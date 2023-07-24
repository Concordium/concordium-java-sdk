package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.exceptions.ClientInitializationException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Shuts down the Network Layer for the Node.
 */
@Command(name = "Shutdown", mixinStandardHelpOptions = true)
public class Shutdown implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Shutdown()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException, BlockNotFoundException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2
                .from(connection)
                .shutdown();

        return 0;
    }
}
