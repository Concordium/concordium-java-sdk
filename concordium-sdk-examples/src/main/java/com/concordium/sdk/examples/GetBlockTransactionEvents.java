package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blockitemsummary.Summary;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * Gets the block transaction events by {@link com.concordium.sdk.examples.GetBlockTransactionEvents} and prints it to the console.
 */
@CommandLine.Command(name = "GetBlockTransactionEvents", mixinStandardHelpOptions = true)
public class GetBlockTransactionEvents implements Callable<Integer> {
    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;


    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        Iterator<Summary> blockTransactionEvents = ClientV2
                .from(connection)
                .getBlockTransactionEvents(BlockQuery.LAST_FINAL);

        while(blockTransactionEvents.hasNext()) {
            Summary event = blockTransactionEvents.next();
            System.out.println(event);
        }

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new com.concordium.sdk.examples.GetBlockTransactionEvents()).execute(args);
        System.exit(exitCode);
    }
}
