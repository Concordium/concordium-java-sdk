package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.transactions.AccountNonce;
import com.concordium.sdk.types.AccountAddress;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetNextAccountSequenceNumber", mixinStandardHelpOptions = true)
public class GetNextAccountSequenceNumber implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;
    private static final AccountAddress ACCOUNT_ADDRESS = AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();


        AccountNonce nextAccountSequenceNumber = ClientV2
                .from(connection)
                .getNextAccountSequenceNumber(ACCOUNT_ADDRESS);

        System.out.println(nextAccountSequenceNumber);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetNextAccountSequenceNumber()).execute(args);
        System.exit(exitCode);
    }
}
