package com.example.android_sdk_example.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android_sdk_example.Storage
import com.example.android_sdk_example.identity_object.IdentityObject
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class IdentityActivity : ComponentActivity() {
    private fun createAccount (seedPhrase: String, identity: IdentityObject) {
        // TODO Create and save account + go to AccountActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(getSharedPreferences("EXAMPLE", MODE_PRIVATE))
        val seedPhrase = storage.seedPhrase.get()
        val identity = Gson().fromJson(storage.identity.get(), IdentityObject::class.java);

        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IdentityView(identity, createAccount = {
                        if (seedPhrase != null) {
                            createAccount(seedPhrase, it)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun IdentityView(identity: IdentityObject, createAccount: (identity: IdentityObject) -> Unit) {
    val attributes = identity.attributeList.chosenAttributes
    AndroidsdkexampleTheme {
    Column {
        Text(text = "Name: ${attributes.get("firstName")} ${attributes.get("lastName")}")
        Text(text = "Nationality: ${attributes.get("nationality")}")
        Button(onClick = { createAccount(identity) }) {
            Text(text = "Create account")
        }
    }
}
}

