package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
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

    @Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;
    private Hash blockHash = Hash.from("c0e24fbf97833827eedbc0a9297eaaae16a10c28c3ace0e3a3937cb50ab0c74a");

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        val client = ClientV2.from(connection);
        val getBlockItemStatus = client
                .getBlockItemStatus(blockHash);

        System.out.println(getBlockItemStatus);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockItemStatus()).execute(args);
        System.exit(exitCode);
    }
}
