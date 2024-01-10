package com.example.android_sdk_example.activities

import android.os.Bundle
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.android_sdk_example.ConcordiumWalletProxyService
import com.example.android_sdk_example.identity_object.IdentityObject
import com.example.android_sdk_example.ui.theme.AndroidsdkexampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


data class IdentityResponse(
    val status: String,
    val token: IdentityWrapper,
    val detail: String,
)

data class IdentityWrapper(val identityObject: VersionedIdentity)

data class VersionedIdentity(
    val v: Number,
    val value: IdentityObject
)

interface IdentityProviderBackend {
    @GET
    fun getIdentity(@Url url: String): Call<IdentityResponse>
}

fun getIdentity(identityUrl: String): IdentityObject {
    val retrofit =
        Retrofit.Builder()
            .baseUrl("http://dummy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val backend = retrofit.create(IdentityProviderBackend::class.java)

    val response = backend.getIdentity(identityUrl).execute()
    if (response.isSuccessful) {
        response.body()?.let{ return it.token.identityObject.value }
    }
    throw Exception(response.message())
}
class IdentityActivity : ComponentActivity() {
    private fun createAccount (seedPhrase: String, identity: IdentityObject) {
        // TODO Create and save account + go to AccountActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mPrefs = getSharedPreferences("EXAMPLE", ComponentActivity.MODE_PRIVATE)
        val seedPhrase = mPrefs.getString("seed_phrase", "");
        val identityUrl = mPrefs.getString("identity_url", "");
        setContent {
            AndroidsdkexampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IdentityView(identityUrl!!, createAccount = {
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
fun IdentityView(identityUrl: String, createAccount: (identity: IdentityObject) -> Unit) {
    var identity by remember { mutableStateOf<IdentityObject?>(null) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            identity = getIdentity(identityUrl);
        }
    }

    if (identity == null) {
        AndroidsdkexampleTheme {
            Column {
            Text(text = "Loading Identity")
            }
        }
    } else {
        println(identity)
        val attributes: Map<String,String> = identity!!.attributeList.chosenAttributes
        AndroidsdkexampleTheme {
        Column {
            Text(text = "Name: ${attributes.get("firstName")} ${attributes.get("lastName")}")
            Text(text = "Nationality: ${attributes.get("nationality")}")
            Button(onClick = { createAccount(identity!!) }) {
                Text(text = "Create account")
            }
        }

    }
    }
}

