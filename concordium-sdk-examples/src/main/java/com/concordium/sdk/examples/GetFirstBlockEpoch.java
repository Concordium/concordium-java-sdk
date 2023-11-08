package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.requests.EpochQuery;
import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.transactions.Hash;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20002" if not specified).
 * Retrieves and prints the {@link Hash} of the first finalized block for the Epoch 2 at genesis index 5.
 */
@CommandLine.Command(name = "GetFirstBlockEpoch", mixinStandardHelpOptions = true)
public class GetFirstBlockEpoch implements Callable<Integer> {
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
        Hash hash = client.getFirstBlockEpoch(EpochQuery.RELATIVE_EPOCH(5, Epoch.from(2)));
        System.out.println(hash);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetFirstBlockEpoch()).execute(args);
        System.exit(exitCode);
    }
}
