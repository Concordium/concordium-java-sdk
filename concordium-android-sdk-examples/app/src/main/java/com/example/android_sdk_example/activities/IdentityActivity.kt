package com.example.android_sdk_example.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme

interface Identity {
    val firstName: String
    val lastName: String
    val nationality: String
}
class IdentityActivity : ComponentActivity() {
    private fun getIdentity(): Identity {
        // TODO: Actually load identity from prefs
        return object: Identity {
            override val firstName = "John"
            override val lastName = "Doe"
            override val nationality = "DK"
        }
    }

    private fun createAccount (seedPhrase: String, identity: Identity) {
        // TODO Create and save account + go to AccountActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mPrefs = getSharedPreferences(localClassName, ComponentActivity.MODE_PRIVATE)
        val seedPhrase = mPrefs.getString("seed_phrase", "");
        val identity = getIdentity()

        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IdentityView(identity, createAccount = {
                        if (seedPhrase != null) {
                            createAccount(seedPhrase, identity)
                        }
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentityView(identity: Identity, createAccount: () -> Unit) {
    AndroidsdkexampleTheme {
        Column {
            Text(text = "Name: ${identity.firstName} ${identity.lastName}")
            Text(text = "Nationality: ${identity.nationality}")
                Button(onClick = createAccount) {
                    Text(text = "Create account")
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IdentityActivityPreview() {
    val identity = object: Identity {
        override val firstName = "John"
        override val lastName = "Doe"
        override val nationality = "DK"
    }
    val context = LocalContext.current
    IdentityView(identity, createAccount = { Toast.makeText(context, "Creating account", Toast.LENGTH_SHORT).show() })
}
