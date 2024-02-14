package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import lombok.var;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Traverse blocks in a given span and query statistics.
 * For each block print
 *  - Block hash
 *  - Block slot time
 *  - Receive time of the block at the given node
 *  - Arrive time of the block at the given node
 *  - Difference between receive and slot times
 *  - Difference between arrive and slot times
 *  - Number if events associated with payday
 *  - Whether the block contains a finalization record
 *  - The number of transactions included in the block
 */
@CommandLine.Command(name = "BlockStats", mixinStandardHelpOptions = true)
public class BlockStats implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @CommandLine.Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;

    @CommandLine.Option(
            names = {"--from"},
            description = "Starting relative block height",
            defaultValue = "0"
    )
    private long from;

    @CommandLine.Option(
            names = {"--to"},
            description = "End absolute block heigh"
    )
    private long to;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .timeout(timeout)
                .build();
        var client = ClientV2.from(connection);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FindAccount()).execute(args);
        System.exit(exitCode);
    }
}

