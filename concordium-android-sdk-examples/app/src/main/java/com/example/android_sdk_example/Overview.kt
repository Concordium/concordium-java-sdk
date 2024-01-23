package com.example.android_sdk_example

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_sdk_example.activities.AccountActivity
import com.example.android_sdk_example.activities.IdentityActivity
import com.example.android_sdk_example.activities.IssueIdentityActivity
import com.example.android_sdk_example.activities.RecoverIdentityActivity
import com.example.android_sdk_example.activities.SeedPhraseActivity
import com.example.android_sdk_example.ui.Container

/**
 * Preview that an overview menu, where each activity can be accessed directly.
 * Mostly useful for testing purposes.
 */
@Preview(showBackground = true)
@Composable
fun Overview() {
    val context = LocalContext.current

    fun gotoActivity(activity: Class<out ComponentActivity>) {
        val myIntent = Intent(context, activity)
        context.startActivity(myIntent)
    }

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
