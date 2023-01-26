package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import io.grpc.StatusRuntimeException;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;

@Command(name = "GetBlocks", mixinStandardHelpOptions = true)
public class GetBlocks implements Runnable {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "10000")
    private int timeout;

    @lombok.SneakyThrows
    @Override
    public void run() {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();
        ClientV2 clientV2 = ClientV2.from(connection);
        var response = clientV2.getBlocks(timeout);

        try {
            while (response.hasNext()) {
                System.out.println(response.next());
            }
        } catch (StatusRuntimeException ex) {
            System.out.println(String.format("Grpc Error: %s", ex.getStatus().getCode()));
        }

    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlocks()).execute(args);
        System.exit(exitCode);
    }
}
