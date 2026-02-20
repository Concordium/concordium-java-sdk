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
import com.concordium.sdk.transactions.tokens.MintTokenOperation;
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

@Command(name = "MintToken", mixinStandardHelpOptions = true)
public class MintToken implements Callable<Integer> {
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

        String tokenSymbol = "TestTokenSDK";
        TokenOperationAmount amount = new TokenOperationAmount(
                new BigDecimal("10"),
                10
        );

        AccountAddress sender = AccountAddress.from("49jjCXb13jWeXjHmb4k5wLHz6VGzxmWijua5VV99keQCo2uMdU");
        Expiry expiry = Expiry.createNew().addMinutes(5);

        TransactionSigner signer = TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        ED25519SecretKey
                                .from("0ee537b1eb7108a54dce9e506c3316e500c1c9997d3e83c8245cdd5fc326290f")));

        var client = ClientV2.from(connection);
        var senderInfo = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(sender));
        var nonce = senderInfo.getNonce();
        var txnHash = client.sendTransaction(
                TransactionFactory
                        .newTokenUpdate( TokenUpdate
                                .builder()
                                .tokenSymbol(tokenSymbol)
                                .operation(
                                    MintTokenOperation
                                            .builder()
                                            .amount(amount)
                                            .build()
                                ).build()
                        )
                        .nonce(nonce)
                        .expiry(expiry)
                        .sender(sender)
                        .sign(signer)
        );
        System.out.println(txnHash);
        Optional<FinalizedBlockItem> finalizedBlockItem = client.waitUntilFinalized(txnHash, this.timeout);
        System.out.println(finalizedBlockItem);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MintToken()).execute(args);
        System.exit(exitCode);
    }
}
