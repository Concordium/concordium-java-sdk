package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.TLSConfig;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.tokens.CborMemo;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.transactions.tokens.TokenOperationAmount;
import com.concordium.sdk.transactions.tokens.TransferTokenOperation;
import com.concordium.sdk.types.AccountAddress;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name = "SendTokenTransfer", mixinStandardHelpOptions = true)
public class SendTokenTransfer implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "https://grpc.devnet-plt-beta.concordium.com:20000")
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
                .useTLS(TLSConfig.auto())
                .build();

        String tokenSymbol = "TestLists";
        TokenOperationAmount amount = new TokenOperationAmount(
                new BigDecimal("0.01"),
                10
        );
        AccountAddress sender = AccountAddress.from("4m9AzH7oeq2LNZpmBu3uW9KJEimevgBMD79PhTMxJeYVmtRdxR");
        AccountAddress receiver = AccountAddress.from("386L81BpBVrm2cDrnjEqpaGcuveC8FgiH5ZBxSVdvto4ydVFLX");
        Expiry expiry = Expiry.createNew().addMinutes(5);

        TransactionSigner signer = TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        ED25519SecretKey
                                .from("ad42d4f5122f4cc758c27b9c776f2d7635f30e55acb8697bb7a97edb3d7f0d88")));

        var client = ClientV2.from(connection);
        var senderInfo = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(sender));
        var nonce = senderInfo.getNonce();
        var txnHash = client.sendTransaction(
                TransactionFactory
                        .newTokenUpdate()
                        .sender(sender)
                        .payload(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol(tokenSymbol)
                                        .operation(
                                                TransferTokenOperation
                                                        .builder()
                                                        .recipient(new TaggedTokenHolderAccount(receiver))
                                                        .amount(amount)
                                                        .memo(CborMemo.from("You must see a multi-byte white woman scientist emoji: 👩🏻‍🔬"))
                                                        .build()
                                        )
                                        .build()
                        )
                        .nonce(nonce)
                        .expiry(expiry)
                        .signer(signer)
                        .build()
        );
        System.out.println(txnHash);
        Optional<FinalizedBlockItem> finalizedBlockItem = client.waitUntilFinalized(txnHash, this.timeout);
        System.out.println(finalizedBlockItem);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SendTokenTransfer()).execute(args);
        System.exit(exitCode);
    }
}
