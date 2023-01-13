package com.concordium.sdk.examples;

import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.examples.utils.ConsoleStreamObserver;
import com.concordium.sdk.v2.ClientV2;
import com.concordium.sdk.v2.types.ArInfo;
import com.concordium.sdk.v2.types.BlockHashInput;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;

@Command(name = "GetAnonymityRevokers", mixinStandardHelpOptions = true)
public class GetAnonymityRevokersAsync implements Runnable {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:10001")
    private String endpoint;

    @Option(
            names = {"--auth-token"},
            description = "Authentication Token for the node",
            defaultValue = "rpcadmin")
    private String authToken;

    @lombok.SneakyThrows
    @Override
    public void run() {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(Credentials.from(authToken))
                .build();
        ClientV2 clientV2 = ClientV2.from(connection);

        var consoleStreamObserver = new ConsoleStreamObserver<ArInfo>();
        clientV2.getAnonymityRevokers(BlockHashInput.BEST, consoleStreamObserver);

        while (!consoleStreamObserver.isComplete())
            Thread.sleep(500);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetAnonymityRevokersAsync()).execute(args);
        System.exit(exitCode);
    }
}
