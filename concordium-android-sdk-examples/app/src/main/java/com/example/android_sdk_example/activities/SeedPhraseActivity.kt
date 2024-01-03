package com.example.android_sdk_example.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme

// TODO Check SeedPhrase is Valid

class SeedPhraseActivity : ComponentActivity() {
    fun submit(phrase: String, ed: SharedPreferences.Editor) {
        // Save seed phrase in SharedPreferences
        ed.putString("seed_phrase", phrase)
        ed.commit()
        // Go to identity issuance
        val myIntent = Intent(this, IssueIdentityActivity::class.java)
        startActivity(myIntent);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mPrefs = getSharedPreferences(localClassName, MODE_PRIVATE)
        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SeedPhraseView(onSubmit = { submit(it, mPrefs.edit()) })
                }
            }
        }
    }
}

@Composable
fun SeedPhraseView(onSubmit: (phrase: String) -> Unit) {
    var phrase by remember { mutableStateOf("") }

    AndroidsdkexampleTheme {
        Column {
            TextField(
                value = phrase,
                onValueChange = { phrase = it },
                label = { Text("Enter your seed phrase") }
            )
            Button(onClick = { onSubmit(phrase) }) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeedPhraseActivityPreview() {
    val context = LocalContext.current
    SeedPhraseView(onSubmit = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() })
}