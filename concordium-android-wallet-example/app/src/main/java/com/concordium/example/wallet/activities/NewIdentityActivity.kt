package com.concordium.example.wallet.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.concordium.example.wallet.ui.Container

class NewIdentityActivity : ComponentActivity() {
    private fun onContinue(activity: Class<out Activity>) {
        val myIntent = Intent(this, activity)
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NewIdentityView { onContinue(it) }
        }
    }
}

@Composable
fun NewIdentityView(
    onContinue: (Class<out Activity>) -> Unit
) {
    Container {
        Column {
            Button(onClick = { onContinue(IssueIdentityActivity::class.java) }) {
                Text(text = "Create a new identity")
            }
            Button(onClick = { onContinue(RecoverIdentityActivity::class.java) }) {
                Text(text = "Recover an existing identity")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewIdentityActivityPreview() {
    val context = LocalContext.current
    NewIdentityView(onContinue = { Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show() })
}
