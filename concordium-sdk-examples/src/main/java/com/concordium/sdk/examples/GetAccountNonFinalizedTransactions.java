package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.types.AccountAddress;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Gets all the Non-Finalized Account Transactions for {@link GetAccountNonFinalizedTransactions#account}.
 */
@Command(name = "GetAccountNonFinalizedTransactions", mixinStandardHelpOptions = true)
public class GetAccountNonFinalizedTransactions implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @Option(
            names = {"--account"},
            description = "Account Address.",
            defaultValue = "3bkTmK6GBWprhq6z2ukY6dEi1xNoBEMPDyyMQ6j8xrt8yaF7F2")
    private String account;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2
                .from(connection)
                .getAccountNonFinalizedTransactions(AccountAddress.from(account))
                .forEachRemaining(System.out::println);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetAccountNonFinalizedTransactions()).execute(args);
        System.exit(exitCode);
    }
}
