package com.example.android_sdk_example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.concordium.sdk.ClientV2
import com.concordium.sdk.Connection
import com.concordium.sdk.TLSConfig
import com.concordium.sdk.requests.AccountQuery
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.types.AccountAddress
import com.example.android_sdk_example.activities.AccountActivity
import com.example.android_sdk_example.activities.IdentityActivity
import com.example.android_sdk_example.activities.IssueIdentityActivity
import com.example.android_sdk_example.activities.RecoverIdentityActivity
import com.example.android_sdk_example.activities.SeedPhraseActivity
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme

class MainActivity : ComponentActivity() {

    fun gotoActivity(activity: Class<out ComponentActivity>) {
        val myIntent = Intent(this, activity)
        startActivity(myIntent);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidsdkexampleTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Example Wallet")
                        Button(onClick = { gotoActivity(SeedPhraseActivity::class.java) }) {
                            Text(text = "Go to SeedPhrase view")
                        }
                        Button(onClick = { gotoActivity(IssueIdentityActivity::class.java) }) {
                            Text(text = "Go to Identity issuance view")
                        }
                        Button(onClick = { gotoActivity(RecoverIdentityActivity::class.java) }) {
                            Text(text = "Go to Identity recovery view")
                        }
                        Button(onClick = { gotoActivity(IdentityActivity::class.java) }) {
                            Text(text = "Go to Identity view")
                        }
                        Button(onClick = { gotoActivity(AccountActivity::class.java) }) {
                            Text(text = "Go to Account view")
                        }
                    }
                }
            }
        }
    }
}
