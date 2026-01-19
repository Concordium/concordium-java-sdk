package com.concordium.sdk.examples;


import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.tokens.CborMemo;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.transactions.tokens.TokenOperationAmount;
import com.concordium.sdk.transactions.tokens.TransferTokenOperation;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import org.bouncycastle.util.encoders.Hex;
import picocli.CommandLine;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "SponsoredTransaction", mixinStandardHelpOptions = true)
public class SponsoredTransaction implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {

        // Scenario: a buyer pays for goods online with protocol-level token.
        // With a sponsored transaction, it is possible for the merchant
        // to pay the transaction CCD cost, so the buyer only spends tokens.

        // Such a transaction is initiated on the merchant server,
        // then delivered to the buyer's wallet, for example over WalletConnect,
        // for completion and submission.


        // region --- Merchant server side ---

        // Buyer's account address is retrieved on the frontend, over WalletConnect.
        // Having the account address, it's possible to fetch the account nonce from the node.
        AccountAddress buyer = AccountAddress.from("3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE");
        Nonce buyerNonce = getAccountNonce(buyer);

        // Merchant's account address and private key are indeed known on the merchant's server.
        AccountAddress merchant = AccountAddress.from("4Lpoq7uTZJLLotMdSVHzqZFcKwS9SfVp9hZkZiV9QBRiAarEE3");
        TransactionSigner merchantSigner = TransactionSigner.from(
                SignerEntry.from(
                        Index.from(0),
                        Index.from(0),
                        ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9")
                )
        );

        // Payment params.
        TokenOperationAmount tokenAmount = new TokenOperationAmount(UInt64.from("1500"), 2);
        String tokenSymbol = "USDR";
        String memo = "Paying invoice #112155";

        // Merchant's server creates a partially signed sponsored transaction.
        PartiallySignedSponsoredTransaction partiallySignedPaymentTransaction =
                TransactionFactory
                        .newTokenUpdate(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol(tokenSymbol)
                                        .operation(
                                                TransferTokenOperation
                                                        .builder()
                                                        .amount(tokenAmount)
                                                        .memo(CborMemo.from(memo))
                                                        .recipient(new TaggedTokenHolderAccount(merchant))
                                                        .build()
                                        )
                                        .build()
                        )
                        // Buyer is the sender of this payment.
                        .sender(buyer)
                        .nonce(buyerNonce)
                        .expiry(Expiry.createNew().addMinutes(10))
                        // Merchant is the sponsor of this transaction, and also its first signer.
                        .sponsoredBy(merchant)
                        .signAsSponsor(merchantSigner);

        // The transaction is serialized to JSON to be sent to the buyer's wallet.
        String partiallySignedPaymentTransactionJson =
                JsonMapper.INSTANCE.writeValueAsString(partiallySignedPaymentTransaction);

        // endregion --- Merchant server side ---

        System.out.println("Partially signed payment transaction sent from the merchant's server to the buyer's wallet:");
        System.out.println(partiallySignedPaymentTransactionJson);

        // region --- Buyer wallet side ---

        // Payment transaction signed by the merchant is received over WalletConnect.
        partiallySignedPaymentTransaction =
                JsonMapper.INSTANCE.readValue(partiallySignedPaymentTransactionJson, PartiallySignedSponsoredTransaction.class);

        // Transfer payload is parsed from raw bytes
        // to present the transaction in the UI.
        TokenUpdate tokenTransfer = (TokenUpdate) Payload.fromBytes(
                ByteBuffer.wrap(partiallySignedPaymentTransaction.getPayload().getRawBytes())
        );
        showTransferConfirmation(tokenTransfer);

        // Once the transfer is confirmed, the wallet decrypts buyer's private key.
        TransactionSigner buyerSigner = TransactionSigner.from(
                SignerEntry.from(
                        Index.from(0),
                        Index.from(0),
                        ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")
                )
        );

        // The transaction is signed and submitted.
        // Once it is finalized, the merchant's server will process it and finish the purchase.
        AccountTransactionV1 transactionToSubmit =
                TransactionFactory
                        .completeSponsoredTransaction(partiallySignedPaymentTransaction)
                        .asSender(buyerSigner);
        submitTransaction(transactionToSubmit);

        // endregion --- Buyer wallet side ---

        return 0;
    }

    private Nonce getAccountNonce(AccountAddress accountAddress) {
        // In a real scenario, the nonce is fetched from the node.
        return Nonce.from(12345);
    }

    private void showTransferConfirmation(TokenUpdate transfer) {
        // In a real scenario, the transfer summary is presented in the wallet UI.
        System.out.println("Transfer to confirm in the buyer's wallet:");
        System.out.println(transfer.toString());
    }

    private void submitTransaction(AccountTransactionV1 transaction) {
        // In a real scenario, the transaction is submitted to the node.
        System.out.println("Confirmed and submitted transaction:");
        System.out.println(Hex.toHexString(transaction.getBytes()));
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SponsoredTransaction()).execute(args);
        System.exit(exitCode);
    }
}
