package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import picocli.CommandLine;

import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20001" if not specified).
 * Unbans the peer specified by the {@link InetAddress} 3.97.143.216
 */
@CommandLine.Command(name = "UnbanPeer", mixinStandardHelpOptions = true)
public class UnbanPeer implements Callable<Integer> {

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

        InetAddress peerToBan = InetAddress.getByName("3.97.143.216");
        ClientV2
                .from(connection)
                .unbanPeer(peerToBan);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new UnbanPeer()).execute(args);
        System.exit(exitCode);
    }
}
