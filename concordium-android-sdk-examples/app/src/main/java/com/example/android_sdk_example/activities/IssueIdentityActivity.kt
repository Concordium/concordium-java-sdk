package com.example.android_sdk_example.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.example.android_sdk_example.Constants
import com.example.android_sdk_example.Storage
import com.example.android_sdk_example.createIssuanceRequest
import com.example.android_sdk_example.services.ConcordiumClientService
import com.example.android_sdk_example.services.ConcordiumWalletProxyService
import com.example.android_sdk_example.services.wallet_proxy.IdentityProvider
import com.example.android_sdk_example.ui.Container
import com.example.android_sdk_example.ui.Menu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IssueIdentityActivity : ComponentActivity() {
    private fun launchChromeCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun getIssuanceUrl(provider: IdentityProvider, request: String): String {
        val baseUrl = provider.metadata.issuanceStart
        val delimiter = if (baseUrl.contains('?')) "&" else "?"
        return "${baseUrl}${delimiter}response_type=code&redirect_uri=${Constants.CALLBACK_URL}.CALLBACK_URL&scope=identity&state=$request"
    }

    private fun submit(
        provider: IdentityProvider,
        global: CryptographicParameters,
        storage: Storage
    ) {
        val request = createIssuanceRequest(storage.getWallet(), provider, global)
        val url = getIssuanceUrl(provider, request)
        launchChromeCustomTab(url)
        storage.identityProviderIndex.set(provider.ipInfo.ipIdentity.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(applicationContext)
        val global = ConcordiumClientService.getClient().getCryptographicParameters(BlockQuery.BEST)

        intent.data?.let {
            println(it)
            handleCodeUri(it, storage)
        }

        setContent {
            IssueIdentityView(onSubmit = { submit(it, global, storage) })
        }
    }

    private fun handleCodeUri(uri: Uri, storage: Storage) {
        if (uri.toString().startsWith(Constants.CALLBACK_URL)) {
            val fragment = uri.fragment
            val fragmentParts = fragment?.split("=")
            if (!fragmentParts.isNullOrEmpty() && fragmentParts[0] == "code_uri") {
                // Save identity object location url
                val codeUri = uri.toString().split("#code_uri=").last()
                storage.identityUrl.set(codeUri)
                // Go to identity confirmation
                val myIntent = Intent(this, IdentityConfirmationActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(myIntent)
            } else if (!fragmentParts.isNullOrEmpty() && fragmentParts[0] == "error") {
                Toast.makeText(
                    this,
                    "Identity creation failed: " + fragmentParts[1],
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val storage = Storage(applicationContext)
        intent?.data?.let { handleCodeUri(it, storage) }
    }
}

fun getProviderName(provider: IdentityProvider): String {
    return provider.ipInfo.description.name
}

@Composable
fun IssueIdentityView(onSubmit: (provider: IdentityProvider) -> Unit) {
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
            Menu(providers, provider, ::getProviderName) { provider = it }
            Button(onClick = { provider?.let { onSubmit(it) } }) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IssueIdentityActivityPreview() {
    val context = LocalContext.current
    IssueIdentityView(onSubmit = {
        Toast.makeText(context, getProviderName(it), Toast.LENGTH_SHORT).show()
    })
}
