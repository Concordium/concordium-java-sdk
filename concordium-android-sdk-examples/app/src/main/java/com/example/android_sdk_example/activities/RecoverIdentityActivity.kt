package com.example.android_sdk_example.activities

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
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.example.android_sdk_example.Requests
import com.example.android_sdk_example.Storage
import com.example.android_sdk_example.services.ConcordiumClientService
import com.example.android_sdk_example.services.ConcordiumWalletProxyService
import com.example.android_sdk_example.services.IdentityFetcherService
import com.example.android_sdk_example.services.wallet_proxy.IdentityProvider
import com.example.android_sdk_example.ui.Container
import com.example.android_sdk_example.ui.Menu
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecoverIdentityActivity : ComponentActivity() {
    private fun getRecoverUrl(provider: IdentityProvider, request: String): String {
        val baseUrl = provider.metadata.recoveryStart
        return Uri.parse(baseUrl!!).buildUpon().appendQueryParameter("state", request).build()
            .toString()
    }

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

        // Then continue to identity view
        val myIntent = Intent(this, IdentityActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
                    } catch (e: Exception) {
                        println(e.message)
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
