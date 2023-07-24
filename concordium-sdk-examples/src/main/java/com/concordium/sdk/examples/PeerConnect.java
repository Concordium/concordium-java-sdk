package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import picocli.CommandLine;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20001" if not specified).
 * Attempts to connect to the Peer on "192.0.2.0:8888".
 */
@CommandLine.Command(name = "PeerConnect", mixinStandardHelpOptions = true)
public class PeerConnect implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws Exception {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        InetSocketAddress peer = new InetSocketAddress("192.0.2.0", 8888);
        ClientV2
                .from(connection)
                .peerConnect(peer);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PeerConnect()).execute(args);
        System.exit(exitCode);
    }
}
