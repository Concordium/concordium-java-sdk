package com.concordium.example.wallet.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.concordium.example.wallet.Storage
import com.concordium.example.wallet.ui.Container
import org.bitcoinj.crypto.MnemonicCode

class SeedPhraseActivity : ComponentActivity() {
    private fun submit(phrase: String, storage: Storage) {
        // Save seed phrase in SharedPreferences
        storage.seedPhrase.set(
            phrase
                .trim()
                .split("\\s".toRegex())
                .joinToString(separator = " ")
        )
        // Go to identity issuance
        val myIntent = Intent(this, NewIdentityActivity::class.java)
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(applicationContext)
        val seedPhrase = storage.seedPhrase.get()

        setContent {
            SeedPhraseView(seedPhrase ?: "", onSubmit = { submit(it, storage) })
        }
    }
}

fun validatePhrase(phrase: String): Boolean {
    return try {
        MnemonicCode.INSTANCE.check(
            phrase
                .trim()
                .split("\\s".toRegex())
        )
        true
    } catch (e: Exception) {
        false
    }
}

@Composable
fun SeedPhraseView(initialPhrase: String, onSubmit: (phrase: String) -> Unit) {
    var phrase by remember { mutableStateOf(initialPhrase) }
    val validPhrase = remember(phrase) { validatePhrase(phrase) }

    Container {
        Column {
            TextField(
                value = phrase,
                onValueChange = { phrase = it },
                label = { Text("Enter your seed phrase") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
            )
            Button(onClick = { onSubmit(phrase) }, enabled = validPhrase) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeedPhraseActivityPreview() {
    val context = LocalContext.current
    SeedPhraseView("", onSubmit = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() })
}
