package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import lombok.val;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetBlocks", mixinStandardHelpOptions = true)
public class GetBlockItems implements Callable<Integer> {
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

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        val client = ClientV2.from(connection);
        client
                .getBlocks(timeout)
                .forEachRemaining(b -> client
                        .getBlockItems(BlockHashInput.GIVEN(b.getBlockHash()))
                        .forEachRemaining(System.out::println));

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockItems()).execute(args);
        System.exit(exitCode);
    }
}
