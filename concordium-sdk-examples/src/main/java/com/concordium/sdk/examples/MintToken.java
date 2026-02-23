package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blockitemstatus.FinalizedBlockItem;
import com.concordium.sdk.transactions.*;
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
            defaultValue = "https://127.0.0.1:7000")
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
                //.useTLS(TLSConfig.auto()) //update this as necessary if using tls or not
                .build();

        String tokenSymbol = "RRR10"; // Replace this with the appropriate token symbol in your node
        TokenOperationAmount amount = new TokenOperationAmount(
                new BigDecimal("1"),
                6 //ensure the decimal precision here matches the value of the decimal during token creation
        );

        //use an address valid in the node being used
        AccountAddress sender = AccountAddress.from("4FbFhx5C5XaSUKdnTdrRVbtNW6rtgYYdKRJtggYGBSiktiymsX");
        Expiry expiry = Expiry.createNew().addMinutes(5);

        //replace with a valid sign key value from the accounts of the node being used
        TransactionSigner signer = TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        ED25519SecretKey
                                .from("b4cef1510079ba946c093826d3cf9cdd3132fa9dd8aaaf9c01442bf12d6614fd")));

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
