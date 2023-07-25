package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.responses.peerlist.PeerInfo;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20001" if not specified).
 * Retrieves and prints the {@link PeerInfo} of connected Peers.
 */
@CommandLine.Command(name = "GetPeersInfo", mixinStandardHelpOptions = true)
public class GetPeersInfo implements Callable<Integer> {
    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @Override
    public Integer call() throws Exception {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2
                .from(connection)
                .getPeersInfo()
                .forEach(System.out::println);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetPeersInfo()).execute(args);
        System.exit(exitCode);
    }
}
