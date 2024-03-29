package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.SpecialOutcome;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20001" if not specified).
 * Retrieves and prints the {@link SpecialOutcome}s for the block {@link BlockQuery#BEST}.
 */
@CommandLine.Command(name = "GetBlockSpecialEvents", mixinStandardHelpOptions = true)
public class GetBlockSpecialEvents implements Callable<Integer> {
    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;


    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2
                .from(connection)
                .getBlockSpecialEvents(BlockQuery.BEST)
                .forEach(System.out::println);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockSpecialEvents()).execute(args);
        System.exit(exitCode);
    }
}
