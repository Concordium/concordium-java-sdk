package com.concordium.sdk.examples;

import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.ClientV2;
import com.google.common.collect.ImmutableList;
import io.grpc.StatusRuntimeException;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;

@Command(name = "GetAnonymityRevokers", mixinStandardHelpOptions = true)
public class GetAnonymityRevokers implements Runnable {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

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
        var arInfos = clientV2.getAnonymityRevokers(BlockHashInput.BEST);

        try {
            ImmutableList
                    .copyOf(arInfos)
                    .forEach(arInfo -> System.out.println(arInfo));
        } catch (StatusRuntimeException ex) {
            System.out.println(String.format("Grpc Error: %s", ex.getStatus().getCode()));
        }

    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetAnonymityRevokers()).execute(args);
        System.exit(exitCode);
    }
}
