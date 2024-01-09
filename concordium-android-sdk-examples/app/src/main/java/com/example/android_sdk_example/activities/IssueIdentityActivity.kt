package com.example.android_sdk_example.activities

import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.core.content.ContextCompat
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey
import com.concordium.sdk.crypto.wallet.ConcordiumHdWallet
import com.concordium.sdk.crypto.wallet.Identity.createIdentityRequest
import com.concordium.sdk.crypto.wallet.IdentityRequestCommon
import com.concordium.sdk.crypto.wallet.IdentityRequestInput
import com.concordium.sdk.crypto.wallet.Network
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo.IdentityProviderInfoBuilder
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.example.android_sdk_example.ConcordiumClientService
import com.example.android_sdk_example.ConcordiumWalletProxyService
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme
import com.example.android_sdk_example.wallet_proxy.IdentityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bitcoinj.crypto.MnemonicCode

class IssueIdentityActivity : ComponentActivity() {
    @OptIn(ExperimentalStdlibApi::class)
    private fun createRequest(seed: String, provider: IdentityProvider, global: CryptographicParameters): String {

        println("test")
        println(seed)
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

        val common: IdentityRequestCommon = IdentityRequestCommon.builder()
            .globalContext(global)
            .ipInfo(ipInfo)
            .arsInfos(arsInfos)
            .arThreshold(2).build()

        val seedAsHex = Mnemonics.MnemonicCode(seed.toCharArray()).toSeed().toHexString()
        println(seedAsHex)
        val wallet: ConcordiumHdWallet = ConcordiumHdWallet.fromHex(seedAsHex, Network.Mainnet)
        val idCredSec: String = wallet.getIdCredSec(0, 0)
        val prfKey: String = wallet.getPrfKey(0, 0)
        val blindingRandomness: String = wallet.getSignatureBlindingRandomness(0, 0)

        val input: IdentityRequestInput = IdentityRequestInput.builder()
            .common(common)
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

    private fun submit(seed: String, provider: IdentityProvider, global: CryptographicParameters) {
        // TODO: Create identity request
        val request = createRequest(seed, provider, global)
        val baseUrl = provider.metadata.issuanceStart
        val delimiter = if (baseUrl.contains('?')) "&" else "?"
        val CALLBACK_URL = "concordiumwallet-example://identity-issuer/callback"
        val url = "${baseUrl}${delimiter}response_type=code&redirect_uri=$CALLBACK_URL&scope=identity&state=$request"
        launchChromeCustomTab(url)
        return
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mPrefs = getSharedPreferences("EXAMPLE", ComponentActivity.MODE_PRIVATE)
        val seedPhrase = mPrefs.getString("seed_phrase", "");
        val global = ConcordiumClientService.getClient().getCryptographicParameters(BlockQuery.BEST);

        println("1" + seedPhrase)
        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IssueIdentityView(onSubmit = { provider -> seedPhrase?.let { submit(it, provider, global) }})
                }
            }
        }
    }
}

fun getProviderName(provider: IdentityProvider): String {
    return provider.ipInfo.ipDescription.name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueIdentityView(onSubmit: (provider: IdentityProvider) -> Unit) {
    var provider by remember { mutableStateOf<IdentityProvider?>(null) }
    var expanded by remember { mutableStateOf(false) }
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
            ExposedDropdownMenuBox (
            expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                ) {
                provider?.let {
                    TextField(
                        value = getProviderName(it),
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    providers.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = getProviderName(item)) },
                            onClick = {
                                expanded = false
                                provider = item
                            },
                        )
                    }
                }

        }
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
