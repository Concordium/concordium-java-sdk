package com.example.android_sdk_example.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.concordium.sdk.crypto.wallet.Credential
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentDetails
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentSerializationContext
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject
import com.concordium.sdk.responses.accountinfo.credential.AttributeType
import com.concordium.sdk.transactions.CredentialRegistrationId
import com.concordium.sdk.transactions.Index
import com.concordium.sdk.transactions.TransactionExpiry
import com.concordium.sdk.types.AccountAddress
import com.example.android_sdk_example.Constants
import com.example.android_sdk_example.Requests
import com.example.android_sdk_example.Storage
import com.example.android_sdk_example.services.ConcordiumClientService
import com.example.android_sdk_example.ui.Container
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.codec.binary.Hex
import java.util.Collections
import java.util.Date


class IdentityActivity : ComponentActivity() {

    /**
     * Creates a new account, and then navigates to the account activity.
     * @param identity the identity object that the account will be created for
     * @param ipIdentity the index of the identity's provider
     * @param storage storage delegator to get the wallet and to save the account address
     */
    private fun createAccount(
        identity: IdentityObject,
        ipIdentity: Int,
        storage: Storage
    ) {
        val wallet = storage.getWallet()

        val credential = Requests.createCredentialRequest(
            wallet,
            identity,
            ipIdentity,
        )

        val expiry = TransactionExpiry.fromLong(Date().time + 360)

        val credentialDeploymentSignDigest = Credential.getCredentialDeploymentSignDigest(
            CredentialDeploymentDetails(
                credential.unsignedCdi,
                expiry
            )
        )
        val signingKey = wallet.getAccountSigningKey(
            ipIdentity,
            Constants.IDENTITY_INDEX,
            Constants.CREDENTIAL_COUNTER
        )
        val signature = signingKey.sign(credentialDeploymentSignDigest)
        val context = CredentialDeploymentSerializationContext(
            credential.unsignedCdi,
            Collections.singletonMap(Index.from(0), Hex.encodeHexString(signature))
        )
        val credentialPayload = Credential.serializeCredentialDeploymentPayload(context)
        ConcordiumClientService.getClient()
            .sendCredentialDeploymentTransaction(expiry, credentialPayload)

        // Save the address of the account
        val address =
            AccountAddress.from(CredentialRegistrationId.from(credential.unsignedCdi.credId))
        storage.accountAddress.set(address.encoded())

        // Then continue to account view
        val myIntent = Intent(this, AccountActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(applicationContext)
        val identityProvider = storage.identityProviderIndex.get()
        val identity =
            jacksonObjectMapper().readValue(storage.identity.get(), IdentityObject::class.java)

        setContent {
            IdentityView(identity, createAccount = {
                createAccount(it, identityProvider!!.toInt(), storage)
            })
        }
    }
}

@Composable
fun IdentityView(identity: IdentityObject, createAccount: (identity: IdentityObject) -> Unit) {
    var creatingAccount by remember { mutableStateOf(false) }
    val attributes = identity.attributeList.chosenAttributes
    val context = LocalContext.current

    Container {
        Column {
            Text(text = "Name: ${attributes[AttributeType.FIRST_NAME]} ${attributes[AttributeType.LAST_NAME]}")
            Text(text = "Nationality: ${attributes[AttributeType.NATIONALITY]}")
            Button(onClick = {
                creatingAccount = true
                try {
                    createAccount(identity)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
                creatingAccount = false
            }, enabled = !creatingAccount) {
                Text(text = "Create account")
            }
        }
    }
}
