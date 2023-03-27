package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.responses.transactionstatus.TransactionStatus;
import com.concordium.sdk.transactions.Hash;
import lombok.val;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetBlockItemStatus", mixinStandardHelpOptions = true)
public class GetBlockItemStatus implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    private final Hash blockHashFailure = Hash.from("d1bf95c1a2acc0947ec3900040c2ba172071aa759adf269c55ebb896aa6825c2");

    private final Hash blockHashSuccess = Hash.from("1ea074f0e12e18684f2d6bbf2039c6db32d2fd5c28e6ba74c8e92f36e88b1901");

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        val client = ClientV2.from(connection);
        TransactionStatus getBlockItemStatusFailure = client
                .getBlockItemStatus(blockHashFailure);

        System.out.println(getBlockItemStatusFailure);

        TransactionStatus getBlockItemStatusSuccess = client
                .getBlockItemStatus(blockHashSuccess);

        System.out.println(getBlockItemStatusSuccess);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockItemStatus()).execute(args);
        System.exit(exitCode);
    }
}
