package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.Timestamp;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20002" if not specified).
 * Retrieves and prints the {@link Timestamp} of the projected earliest time at which baker with id = 1 will be required to bake a block.
 */
@CommandLine.Command(name = "GetBakerEarliestWinTime", mixinStandardHelpOptions = true)
public class GetBakerEarliestWinTime implements Callable<Integer> {
    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @Override
    public Integer call() throws Exception {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2 client = ClientV2.from(connection);
        Timestamp timestamp = client.getBakerEarliestWinTime(BakerId.from(1));
        System.out.println(timestamp);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBakerEarliestWinTime()).execute(args);
        System.exit(exitCode);
    }
}
