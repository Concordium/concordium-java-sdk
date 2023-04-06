package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.BlockNotFoundException;
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

    private final Hash blockHashFailure = Hash.from("082db672088ca94e6da7bf8365206b1abb0a4db3c86778953f04286df7e62ba9");

    private final Hash blockHashSuccess = Hash.from("f99188206161e2fe37a43f89acdfb22864ef63266616d27f4e083b0a97d3ba6a");

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException, BlockNotFoundException {
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
