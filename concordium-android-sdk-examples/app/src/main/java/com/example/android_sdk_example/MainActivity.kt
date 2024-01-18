package com.example.android_sdk_example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android_sdk_example.activities.AccountActivity
import com.example.android_sdk_example.activities.IdentityActivity
import com.example.android_sdk_example.activities.IssueIdentityActivity
import com.example.android_sdk_example.activities.RecoverIdentityActivity
import com.example.android_sdk_example.activities.SeedPhraseActivity
import com.example.android_sdk_example.ui.Container

class MainActivity : ComponentActivity() {

    private fun gotoActivity(activity: Class<out ComponentActivity>) {
        val myIntent = Intent(this, activity)
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Container {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
