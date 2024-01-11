package com.example.android_sdk_example.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import com.concordium.sdk.crypto.bls.BLSSecretKey
import com.concordium.sdk.crypto.wallet.ConcordiumHdWallet
import com.concordium.sdk.crypto.wallet.Identity.createIdentityRecoveryRequest
import com.concordium.sdk.crypto.wallet.IdentityRecoveryRequestInput
import com.concordium.sdk.crypto.wallet.Network
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.example.android_sdk_example.ConcordiumClientService
import com.example.android_sdk_example.ConcordiumWalletProxyService
import com.example.android_sdk_example.Storage
import com.example.android_sdk_example.identity_object.IdentityFetcherService
import com.example.android_sdk_example.ui.Menu
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme
import com.example.android_sdk_example.wallet_proxy.IdentityProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Url
import java.net.URL
import java.util.Calendar
import java.util.Date

class RecoverIdentityActivity : ComponentActivity() {
    @OptIn(ExperimentalStdlibApi::class)
    private fun createRequest(
        seed: String,
        provider: IdentityProvider,
        global: CryptographicParameters
    ): String {
        val ipInfo = provider.toIdentityProviderInfo()
        val seedAsHex = Mnemonics.MnemonicCode(seed.toCharArray()).toSeed().toHexString()
        val wallet: ConcordiumHdWallet = ConcordiumHdWallet.fromHex(seedAsHex, Network.Mainnet)
        val providerIndex: Long = provider.ipInfo.ipIdentity.toLong()
        // In a real wallet this would not be hardcoded, to allow multiple identities from the same provider
        val identityIndex: Long = 0
        val idCredSec: String = wallet.getIdCredSec(providerIndex, identityIndex)

        val input = IdentityRecoveryRequestInput.builder()
            .globalContext(global)
            .ipInfo(ipInfo)
            .idCredSec(BLSSecretKey.from(idCredSec))
            .timestamp(java.time.Instant.now().epochSecond)
            .build()

        return createIdentityRecoveryRequest(input)
    }

    private fun getRecoverUrl(provider: IdentityProvider, request: String): String {
        val baseUrl = provider.metadata.recoveryStart
        return Uri.parse(baseUrl!!).buildUpon().appendQueryParameter("state", request).build().toString()
    }

    private suspend fun submit(
        seed: String,
        provider: IdentityProvider,
        global: CryptographicParameters,
        storage: Storage
    ) {
        val request = createRequest(seed, provider, global)
        val url = getRecoverUrl(provider, request)
        val identity = IdentityFetcherService().getFromRecovery(url)

        storage.identityProviderIndex.set(provider.ipInfo.ipIdentity.toString())
        storage.identity.set(Gson().toJson(identity))

        // Then continue to identity view
        val myIntent = Intent(this, IdentityActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(myIntent);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(getSharedPreferences("EXAMPLE", MODE_PRIVATE))
        val seedPhrase = storage.seedPhrase.get()
        val global =
            ConcordiumClientService.getClient().getCryptographicParameters(BlockQuery.BEST);
        storage.identityProviderIndex.set("0")

        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecoverIdentityView(onSubmit = { provider ->
                        seedPhrase?.let {
                            CoroutineScope(Dispatchers.Default).launch {
                                submit(
                                    it,
                                    provider,
                                    global,
                                    storage
                                )
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun RecoverIdentityView(onSubmit: (provider: IdentityProvider) -> Unit) {
    var provider by remember { mutableStateOf<IdentityProvider?>(null) }
    val providers = remember {
        mutableStateListOf<IdentityProvider>()
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            providers.addAll(ConcordiumWalletProxyService(context).getIdentityProviderInfo());
            provider = providers.first()
        }
    }

    AndroidsdkexampleTheme {
        Column {
            Text(text = "Recovery")
            Menu(providers, provider, ::getProviderName, { provider = it })
            Button(onClick = { provider?.let { onSubmit(it) } }) {
                Text(text = "Submit")
            }
        }
    }
}
