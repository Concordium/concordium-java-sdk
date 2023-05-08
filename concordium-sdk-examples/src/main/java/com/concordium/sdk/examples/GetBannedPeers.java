package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20001" if not specified).
 * Retrieves and prints the {@link java.net.InetAddress} of the banned peers.
 */
@CommandLine.Command(name = "GetBannedPeers", mixinStandardHelpOptions = true)
public class GetBannedPeers implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws Exception {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        ClientV2
                .from(connection)
                .getBannedPeers()
                .forEach(System.out::println);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBannedPeers()).execute(args);
        System.exit(exitCode);
    }
}
