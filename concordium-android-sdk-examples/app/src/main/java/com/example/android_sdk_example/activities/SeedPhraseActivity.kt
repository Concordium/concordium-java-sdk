package com.example.android_sdk_example.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cash.z.ecc.android.bip39.Mnemonics
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme

// TODO Check SeedPhrase is Valid
import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import com.example.android_sdk_example.Storage

class SeedPhraseActivity : ComponentActivity() {
    fun submit(phrase: String, storage: Storage) {
        // Save seed phrase in SharedPreferences
        storage.seedPhrase.set(phrase)
        // Go to identity issuance
        val myIntent = Intent(this, IssueIdentityActivity::class.java)
        startActivity(myIntent);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(getSharedPreferences("EXAMPLE", MODE_PRIVATE))
        val seedPhrase = storage.seedPhrase.get()

        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SeedPhraseView(seedPhrase ?: "", onSubmit = { submit(it, storage) })
                }
            }
        }
    }
}

fun validatePhrase(phrase: String): Boolean {
    return try {
        MnemonicCode(phrase.toCharArray()).validate()
        true;
    } catch (e: Exception) {
        false;
    }
}

@Composable
fun SeedPhraseView(initialPhrase: String, onSubmit: (phrase: String) -> Unit) {
    var phrase by remember { mutableStateOf<String>(initialPhrase) }
    var validPhrase = remember(phrase) { validatePhrase(phrase) }

    AndroidsdkexampleTheme {
        Column {
            TextField(
                value = phrase,
                onValueChange = { phrase = it },
                label = { Text("Enter your seed phrase") }
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
    SeedPhraseView("",onSubmit = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() })
}