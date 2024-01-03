package com.example.android_sdk_example.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters
import com.example.android_sdk_example.ConcordiumClientService
import com.example.android_sdk_example.ConcordiumWalletProxyService
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme
import com.example.android_sdk_example.wallet_proxy.IdentityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class IssueIdentityActivity : ComponentActivity() {
    private fun submit(provider: IdentityProvider, global: CryptographicParameters) {
        // TODO: Create identity request
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // val mPrefs = getSharedPreferences(localClassName, ComponentActivity.MODE_PRIVATE)
        // val seedPhrase = mPrefs.getString("seed_phrase", "");
        val global = ConcordiumClientService.getClient().getCryptographicParameters(BlockQuery.BEST);

        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IssueIdentityView(onSubmit = { submit(it, global) })
                }
            }
        }
    }
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

    println(providers)
    AndroidsdkexampleTheme {
        Column {
            ExposedDropdownMenuBox (
            expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                ) {
                provider?.let {
                    TextField(
                        value = it.ipInfo.ipDescription.name,
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
                            text = { Text(text = item.ipInfo.ipDescription.name) },
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
    IssueIdentityView(onSubmit = { Toast.makeText(context, it.ipInfo.ipDescription.name, Toast.LENGTH_SHORT).show() })
}
