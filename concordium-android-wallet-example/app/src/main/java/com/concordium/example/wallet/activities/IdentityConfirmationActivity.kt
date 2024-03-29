package com.concordium.example.wallet.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject
import com.concordium.example.wallet.Storage
import com.concordium.example.wallet.services.IdentityFetcherService
import com.concordium.example.wallet.ui.Container
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Activity that blocks until the identity is created.
 * Display the error if the identity issuance failed
 * Navigates to the identity page if the issuance succeeded.
 */
class IdentityConfirmationActivity : ComponentActivity() {
    private fun onDone(identity: IdentityObject, storage: Storage) {
        // Save the identity object
        storage.identity.set(jacksonObjectMapper().writeValueAsString(identity))
        // Go to the identity page
        val myIntent = Intent(this, IdentityActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(applicationContext)
        val identityUrl = storage.identityUrl.get()

        setContent {
            IdentityConfirmationView(
                identityUrl!!
            ) { onDone(it, storage) }
        }
    }
}

@Composable
fun IdentityConfirmationView(identityUrl: String, onFinish: (identity: IdentityObject) -> Unit) {
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                onFinish(IdentityFetcherService().fetch(identityUrl))
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    Container {
        Column {
            Text(text = error ?: "Identity is not ready yet")
        }
    }
}
