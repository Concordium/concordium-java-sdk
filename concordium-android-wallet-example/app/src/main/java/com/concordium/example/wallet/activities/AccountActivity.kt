package com.concordium.example.wallet.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey
import com.concordium.sdk.requests.AccountQuery
import com.concordium.sdk.requests.BlockQuery
import com.concordium.sdk.transactions.AccountNonce
import com.concordium.sdk.transactions.CCDAmount
import com.concordium.sdk.transactions.Expiry
import com.concordium.sdk.transactions.Index
import com.concordium.sdk.transactions.SignerEntry
import com.concordium.sdk.transactions.TransactionFactory
import com.concordium.sdk.transactions.TransactionSigner
import com.concordium.sdk.types.AccountAddress
import com.concordium.example.wallet.Constants
import com.concordium.example.wallet.Storage
import com.concordium.example.wallet.services.ConcordiumClientService
import com.concordium.example.wallet.ui.Container


class AccountActivity : ComponentActivity() {

    /**
     * Sends a simple transfer to the chain
     * @param senderAddress the address which should send the funds
     * @param receiverAddress the address which should receive the funds
     * @param microCCDAmount the amount of funds that should be transferred, given in micro ccd
     * @param signer representation of the account that can sign the transaction
     * @return the hash of the transaction, as a HEX-encoded string
     */
    private fun sendTransfer(
        senderAddress: String,
        receiverAddress: String,
        microCCDAmount: Long,
        signer: TransactionSigner
    ): String {
        val sender = AccountAddress.from(senderAddress)
        val receiver = AccountAddress.from(receiverAddress)
        val amount = CCDAmount.fromMicro(microCCDAmount)
        val expiry = Expiry.createNew().addMinutes(5)

        val client = ConcordiumClientService.getClient()
        val senderInfo = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(sender))
        val nonce = senderInfo.accountNonce
        val transactionHash = client.sendTransaction(
            TransactionFactory.newTransfer()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .nonce(AccountNonce.from(nonce))
                .expiry(expiry)
                .signer(signer)
                .build()
        )
        return transactionHash.asHex()
    }

    /**
     * Creates a TransactionSigner for an standard account that only requires one signature
     * @param privateKey the signing key of the first credential of the account
     */
    private fun getSimpleSigner(privateKey: ED25519SecretKey?): TransactionSigner {
        return TransactionSigner.from(
            SignerEntry.from(
                Index.from(0), Index.from(0),
                privateKey
            )
        );
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = Storage(applicationContext)
        val address = storage.accountAddress.get()
        val providerIndex = storage.identityProviderIndex.get()
        val privateKey = storage.getWallet().getAccountSigningKey(
            providerIndex!!.toInt(),
            Constants.IDENTITY_INDEX,
            Constants.CREDENTIAL_COUNTER
        )

        setContent {
            AccountView(
                accountAddress = address!!,
                sendTransfer = { recipient, amount ->
                    sendTransfer(
                        address,
                        recipient,
                        amount,
                        getSimpleSigner(privateKey)
                    )
                })
        }
    }
}

@Composable
fun Transfer(sendTransfer: (recipient: String, amount: Long) -> Unit) {
    var amount by remember { mutableStateOf("0") }
    var recipient by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(top = 30.dp)) {
        TextField(
            value = recipient,
            onValueChange = { recipient = it },
            label = { Text("Recipient Address") }
        )
        TextField(
            label = { Text("Amount (µϾ)") },
            value = amount,
            onValueChange = { value ->
                amount = value.filter { it.isDigit() }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Button(onClick = {
            try {
                sendTransfer(recipient, amount.toLong())
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }) {
            Text(text = "Send Transfer")
        }
    }
}

@Composable
fun AccountView(accountAddress: String, sendTransfer: (recipient: String, amount: Long) -> Unit) {
    Container {
        Column {
            Text(text = "Your Concordium account")
            Text(text = "Address: $accountAddress")
            DisplayAccountBalance(address = accountAddress)
            Transfer(sendTransfer)
        }
    }
}

@Composable
fun DisplayAccountBalance(address: String, modifier: Modifier = Modifier) {
    val balance = remember(address) {
        try {
            val accountInfo = ConcordiumClientService.getClient().getAccountInfo(
                BlockQuery.BEST,
                AccountQuery.from(AccountAddress.from(address))
            )
            "Ͼ${accountInfo.accountAmount.value.value / 10000}"
        } catch (e: Exception) {
            "Unavailable"
        }
    }
    Text(
        text = "Balance: $balance",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AccountActivityPreview() {
    val address by remember { mutableStateOf("4NkgEGFC483jcdgMUR8QKhhKNsRaeSuDQVmoE5LG6MQNDQvCvf") }
    val context = LocalContext.current
    AccountView(
        accountAddress = address,
        sendTransfer = { recipient: String, amount: Long ->
            Toast.makeText(
                context,
                "Sending Ͼ${amount / 10000} to $recipient",
                Toast.LENGTH_SHORT
            ).show()
        })
}
