package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import picocli.CommandLine;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "PeerDisconnect", mixinStandardHelpOptions = true)
public class PeerDisconnect implements Callable<Integer> {

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

        InetSocketAddress peer = new InetSocketAddress("3.97.143.216", 8888);
        ClientV2
                .from(connection)
                .peerDisconnect(peer);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PeerDisconnect()).execute(args);
        System.exit(exitCode);
    }
}
