package com.example.android_sdk_example.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.tooling.preview.Preview
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey
import com.concordium.sdk.crypto.wallet.ConcordiumHdWallet
import com.concordium.sdk.crypto.wallet.Identity.createIdentityRequest
import com.concordium.sdk.crypto.wallet.IdentityRequestInput
import com.concordium.sdk.crypto.wallet.Network
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.example.android_sdk_example.ConcordiumClientService
import com.example.android_sdk_example.ConcordiumWalletProxyService
import com.example.android_sdk_example.Storage
import com.example.android_sdk_example.ui.Menu
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme
import com.example.android_sdk_example.wallet_proxy.IdentityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IssueIdentityActivity : ComponentActivity() {
    val CALLBACK_URL = "concordiumwallet-example://identity-issuer/callback"

    @OptIn(ExperimentalStdlibApi::class)
    private fun createRequest(seed: String, provider: IdentityProvider, global: CryptographicParameters): String {
        val ipInfo = IdentityProviderInfo.builder()
            .ipIdentity(provider.ipInfo.ipIdentity)
            .ipVerifyKey(PSPublicKey.from(provider.ipInfo.ipVerifyKey))
            .ipCdiVerifyKey(ED25519PublicKey.from(provider.ipInfo.ipCdiVerifyKey))
            .description(provider.ipInfo.ipDescription)
            .build()

        val arsInfos = provider.arsInfos.mapValues { (k,v) ->
            AnonymityRevokerInfo.builder()
                .arIdentity(v.arIdentity)
                .arPublicKey(ElgamalPublicKey.from(v.arPublicKey))
                .description(v.arDescription)
                .build() }

        val seedAsHex = Mnemonics.MnemonicCode(seed.toCharArray()).toSeed().toHexString()
        val wallet: ConcordiumHdWallet = ConcordiumHdWallet.fromHex(seedAsHex, Network.Mainnet)
        val providerIndex: Long = provider.ipInfo.ipIdentity.toLong()
        // In a real wallet this would not be hardcoded, to allow multiple identities from the same provider
        val identityIndex: Long = 1
        val idCredSec: String = wallet.getIdCredSec(providerIndex, identityIndex)
        val prfKey: String = wallet.getPrfKey(providerIndex, identityIndex)
        val blindingRandomness: String = wallet.getSignatureBlindingRandomness(providerIndex, identityIndex)

        val input: IdentityRequestInput = IdentityRequestInput.builder()
            .globalContext(global)
            .ipInfo(ipInfo)
            .arsInfos(arsInfos)
            .arThreshold(2)
            .idCredSec(idCredSec)
            .prfKey(prfKey)
            .blindingRandomness(blindingRandomness)
            .build()

        return createIdentityRequest(input)
    }

    private fun launchChromeCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun getIssuanceUrl(provider: IdentityProvider, request: String): String {
        val baseUrl = provider.metadata.issuanceStart
        val delimiter = if (baseUrl.contains('?')) "&" else "?"
        return "${baseUrl}${delimiter}response_type=code&redirect_uri=$CALLBACK_URL&scope=identity&state=$request"
    }

    private fun submit(seed: String, provider: IdentityProvider, global: CryptographicParameters, storage: Storage) {
        val request = createRequest(seed, provider, global)
        val url = getIssuanceUrl(provider, request)
        launchChromeCustomTab(url)
        storage.identityProviderIndex.set(provider.ipInfo.ipIdentity.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(getSharedPreferences("EXAMPLE", MODE_PRIVATE))
        val seedPhrase = storage.seedPhrase.get()
        val global = ConcordiumClientService.getClient().getCryptographicParameters(BlockQuery.BEST);
        storage.identityProviderIndex.set("0")

        intent.data?.let {
            println(it)
            handleCodeUri(it, storage)
        }

        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IssueIdentityView(onSubmit = { provider -> seedPhrase?.let { submit(it, provider, global, storage) }})
                }
            }
        }
    }

    private fun handleCodeUri(uri: Uri, storage: Storage) {
        if (uri.toString().startsWith(CALLBACK_URL)) {
            val fragment = uri.fragment
            val fragmentParts = fragment?.split("=")
            if (!fragmentParts.isNullOrEmpty() && fragmentParts[0] == "code_uri") {
                val codeUri = uri.toString().split("#code_uri=").last();
                storage.identityUrl.set(codeUri)
                val myIntent = Intent(this, IdentityConfirmationActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(myIntent);
            } else if (!fragmentParts.isNullOrEmpty() && fragmentParts[0] == "token") {
                val identity = fragmentParts[1]
                // TODO Handle this case?
            } else if (!fragmentParts.isNullOrEmpty() && fragmentParts[0] == "error") {
                Toast.makeText(this,"Identity creation failed: " + fragmentParts[1], Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val storage = Storage(getSharedPreferences("EXAMPLE", MODE_PRIVATE))
        intent?.data?.let { handleCodeUri(it, storage) }
    }
}

fun getProviderName(provider: IdentityProvider): String {
    return provider.ipInfo.ipDescription.name
}

@Composable
fun IssueIdentityView(onSubmit: (provider: IdentityProvider) -> Unit) {
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
            Menu(providers, provider, ::getProviderName, { provider = it })
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
    IssueIdentityView(onSubmit = { Toast.makeText(context, getProviderName(it), Toast.LENGTH_SHORT).show() })
}
