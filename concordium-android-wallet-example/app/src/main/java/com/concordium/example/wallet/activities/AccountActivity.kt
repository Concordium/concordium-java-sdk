package com.concordium.example.wallet.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey
import com.concordium.sdk.requests.AccountQuery
import com.concordium.sdk.requests.BlockQuery
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
        val nonce = senderInfo.nonce
        val transactionHash = client.sendTransaction(
            TransactionFactory.newTransfer()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .nonce(nonce)
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
fun Transfer(sendTransfer: (recipient: String, amount: Long) -> String) {
    var amount by remember { mutableStateOf("0") }
    var recipient by remember { mutableStateOf("") }
    var transactionHash by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(top = 24.dp)) {
        TextField(
            value = recipient,
            onValueChange = { recipient = it },
            label = { Text("Recipient Address") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            label = { Text("Amount (µϾ)") },
            value = amount,
            onValueChange = { value ->
                amount = value.filter { it.isDigit() }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
        )
        Button(onClick = {
            try {
                transactionHash = sendTransfer(recipient, amount.toLong())
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }) {
            Text(text = "Send Transfer")
        }
        if (transactionHash.isNotBlank()) {
            TransactionHashLink(transactionHash)
        }
    }
}

@Composable
fun TransactionHashLink(transactionHash: String) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val str = "Latest transaction hash: $transactionHash"
        val startIndex = str.indexOf(transactionHash)
        val endIndex = startIndex + transactionHash.length
        append(str)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )
        addStringAnnotation(
            tag = "URL",
            annotation = "https://testnet.ccdscan.io/transactions?dcount=1&dentity=transaction&dhash=$transactionHash",
            start = startIndex,
            end = endIndex
        )
    }

    val uriHandler = LocalUriHandler.current
    ClickableText(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        text = annotatedLinkString,
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}

@Composable
fun AccountView(accountAddress: String, sendTransfer: (recipient: String, amount: Long) -> String) {
    Container {
        Column {
            Text(text = "Your Concordium account", fontSize = 22.sp)
            Text(text = "Address")
            Text(text = accountAddress, fontSize = 14.sp, modifier = Modifier.paddingFromBaseline(0.dp, 14.dp))
            DisplayAccountBalance(address = accountAddress)
            Transfer(sendTransfer)
        }
    }
}

@Composable
fun DisplayAccountBalance(address: String) {
    val balance = remember(address) {
        try {
            val accountInfo = ConcordiumClientService.getClient().getAccountInfo(
                BlockQuery.BEST,
                AccountQuery.from(AccountAddress.from(address))
            )
            "µϾ${accountInfo.accountAmount.value.value}"
        } catch (e: Exception) {
            "Unavailable"
        }
    }
    Column {
        Text(
            text = "Balance"
        )
        Text(text = balance, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun AccountActivityPreview() {
    val address by remember { mutableStateOf("4NkgEGFC483jcdgMUR8QKhhKNsRaeSuDQVmoE5LG6MQNDQvCvf") }
    AccountView(
        accountAddress = address,
        sendTransfer = { recipient: String, amount: Long -> "Dummy hash"})
}
