package com.concordium.example.wallet.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.concordium.sdk.requests.AccountQuery
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.concordium.sdk.transactions.CredentialRegistrationId
import com.concordium.example.wallet.Constants
import com.concordium.example.wallet.Requests
import com.concordium.example.wallet.Storage
import com.concordium.example.wallet.services.ConcordiumClientService
import com.concordium.example.wallet.services.ConcordiumWalletProxyService
import com.concordium.example.wallet.services.IdentityFetcherService
import com.concordium.example.wallet.services.wallet_proxy.IdentityProvider
import com.concordium.example.wallet.ui.Container
import com.concordium.example.wallet.ui.Menu
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecoverIdentityActivity : ComponentActivity() {
    /**
     * Build the url used for the recovery protocol
     * @param provider info about the identity provider which the identity should be recovered from
     * @param request the identity recovery request info needed for recovery, as a JSON string
     */
    private fun getRecoverUrl(provider: IdentityProvider, request: String): String {
        val baseUrl = provider.metadata.recoveryStart
        return Uri.parse(baseUrl!!).buildUpon().appendQueryParameter("state", request).build()
            .toString()
    }

    /**
     * Perform the recovery and save the resulting identity object.
     *
     * @param provider info about the identity provider which the identity should be recovered from
     * @param global the global cryptographic parameters of the current chain
     * @param storage storage delegator to get the wallet and to save the identity and provider index
     */
    private fun recoverIdentity(
        provider: IdentityProvider,
        global: CryptographicParameters,
        storage: Storage
    ) {
        val request = Requests.createRecoveryRequest(storage.getWallet(), provider, global)
        val url = getRecoverUrl(provider, request)
        val identity = IdentityFetcherService().getFromRecovery(url)

        storage.identityProviderIndex.set(provider.ipInfo.ipIdentity.toString())
        storage.identity.set(jacksonObjectMapper().writeValueAsString(identity))
    }

    /**
     *
     * Builds the intent for the next activity.
     * If an account exists for the recovered identity, go directly to the account page.
     * Otherwise go to the identity page.
     * @param storage storage delegator to get the wallet and to save the account address
     * @param global the global cryptographic parameters of the current chain
     * @param ipIdentity index of the identity provider which the identity was recovered from
     */
    private fun goToNext(storage: Storage, global: CryptographicParameters, ipIdentity: Int) {
        val wallet = storage.getWallet()
        val credId = wallet.getCredentialId(ipIdentity, Constants.IDENTITY_INDEX, Constants.CREDENTIAL_COUNTER, global.onChainCommitmentKey.toHex())

        // The recovered identity might already have an associated account. Check if an account info is available, and if so go directly to the account page. Otherwise go to the identity page.
        val myIntent = try {
            val accountInfo = ConcordiumClientService.getClient().getAccountInfo(
                BlockQuery.BEST,
                AccountQuery.from(CredentialRegistrationId.from(credId))
            )
            storage.accountAddress.set(accountInfo.accountAddress.encoded())
            Intent(this, AccountActivity::class.java)
        } catch (e: Exception) {
            Intent(this, IdentityActivity::class.java)
        }
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(applicationContext)
        val global =
            ConcordiumClientService.getClient().getCryptographicParameters(BlockQuery.BEST)

        setContent {
            RecoverIdentityView(onSubmit = { provider ->
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        recoverIdentity(
                            provider,
                            global,
                            storage
                        )
                        goToNext(storage, global, provider.ipInfo.ipIdentity.value)
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }
    }
}

@Composable
fun RecoverIdentityView(onSubmit: (provider: IdentityProvider) -> Unit) {
    var provider by remember { mutableStateOf<IdentityProvider?>(null) }
    val providers = remember {
        mutableStateListOf<IdentityProvider>()
    }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            providers.addAll(ConcordiumWalletProxyService.getIdentityProviderInfo())
            provider = providers.first()
        }
    }

    Container {
        Column {
            Text(text = "Recovery")
            Menu(providers, provider, ::getProviderName) { provider = it }
            Button(onClick = { provider?.let { onSubmit(it) } }) {
                Text(text = "Submit")
            }
        }
    }
}
