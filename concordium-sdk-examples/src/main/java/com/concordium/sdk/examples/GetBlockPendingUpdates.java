package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.PendingUpdateV2;
import com.concordium.sdk.responses.intanceinfo.InstanceInfo;
import com.concordium.sdk.types.ContractAddress;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Gets the Pending Updates of Chain Parameters at the {@link BlockHashInput#BEST} block.
 */
@Command(name = "GetBlockPendingUpdates", mixinStandardHelpOptions = true)
public class GetBlockPendingUpdates implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockPendingUpdates()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2
                .from(connection)
                .getBlockPendingUpdates(BlockHashInput.BEST)
                .forEachRemaining(System.out::println);

        return 0;
    }
}
