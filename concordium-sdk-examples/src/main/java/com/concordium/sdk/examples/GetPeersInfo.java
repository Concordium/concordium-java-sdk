package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.requests.BlockHashInput;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

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
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
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
