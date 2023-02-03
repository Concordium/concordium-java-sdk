package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.requests.BlockHashInput;
import lombok.SneakyThrows;
import lombok.val;
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


    @SneakyThrows
    @Override
    public void run() {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();
        ClientV2 clientV2 = ClientV2.from(connection);
        val arInfos = clientV2.getAnonymityRevokers(BlockHashInput.BEST);
        arInfos.forEachRemaining(System.out::println);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetAnonymityRevokers()).execute(args);
        System.exit(exitCode);
    }

}
