package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AccountAddress;
import lombok.var;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "SendTransferWithMemo", mixinStandardHelpOptions = true)
public class SendTransferWithMemo implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @CommandLine.Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        AccountAddress sender = AccountAddress.from("3WZE6etUvVp1eyhEtTxqZrQaanTAZnZCHEmZmDyCbCwxnmQuPE");
        AccountAddress receiver = AccountAddress.from("4Lpoq7uTZJLLotMdSVHzqZFcKwS9SfVp9hZkZiV9QBRiAarEE3");
        CCDAmount amount = CCDAmount.fromMicro(1000_000);
        Expiry expiry = Expiry.createNew().addMinutes(5);


        TransactionSigner signer = TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        ED25519SecretKey
                                .from("56f60de843790c308dac2d59a5eec9f6b1649513f827e5a13d7038accfe31784")));

        var client = ClientV2.from(connection);
        var senderInfo = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(sender));
        var nonce = senderInfo.getAccountNonce();
        var memo = Memo.from(new byte[]{0, 1, 2, 3});
        var txnHash = client.sendTransaction(TransactionFactory.newTransferWithMemo()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .nonce(AccountNonce.from(nonce))
                .expiry(expiry)
                .signer(signer)
                .memo(memo)
                .build());

        System.out.println("Sending transaction with hash: " + txnHash);
        Optional<FinalizedBlockItem> finalizedTransaction = client.waitUntilFinalized(txnHash,100000);
        finalizedTransaction.ifPresent(finalizedBlockItem -> System.out.println("Transaction finalized in block with hash: " + finalizedBlockItem.getBlockHash()));

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SendTransferWithMemo()).execute(args);
        System.exit(exitCode);
    }
}
