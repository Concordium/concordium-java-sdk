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
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name = "SendSimpleTransfer", mixinStandardHelpOptions = true)
public class SendSimpleTransfer implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @Option(
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
        var nonce = senderInfo.getNonce();
        var txnHash = client.sendTransaction(TransactionFactory.newTransfer()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .nonce(nonce)
                .expiry(expiry)
                .signer(signer)
                .build());
        System.out.println(txnHash);
        Optional<FinalizedBlockItem> finalizedBlockItem = client.waitUntilFinalized(txnHash, this.timeout);
        System.out.println(finalizedBlockItem);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SendSimpleTransfer()).execute(args);
        System.exit(exitCode);
    }
}
