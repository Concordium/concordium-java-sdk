package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.types.ContractAddress;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Calls {@link ClientV2#getInstanceState(BlockQuery, ContractAddress)}. with
 * Contract {@link GetInstanceState#index}, Contract {@link GetInstanceState#subindex} and prints the response to console.
 */
@Command(name = "GetInstanceState", mixinStandardHelpOptions = true)
public class GetInstanceState implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @Option(
            names = {"--index"},
            description = "Contract Address Index.",
            defaultValue = "4341")
    private long index;

    @Option(
            names = {"--subindex"},
            description = "Contract Address Sub-index.",
            defaultValue = "0")
    private long subindex;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetInstanceState()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2
                .from(connection)
                .getInstanceState(BlockQuery.LAST_FINAL, ContractAddress.from(index, subindex))
                .forEachRemaining(System.out::println);

        return 0;
    }
}
