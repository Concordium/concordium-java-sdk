package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "GetBlockFinalizationSummary", mixinStandardHelpOptions = true)
public class GetBlockFinalizationSummary implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        System.out.println(ClientV2.from(connection).getBlockFinalizationSummary(BlockHashInput.BEST));

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockFinalizationSummary()).execute(args);
        System.exit(exitCode);
    }
}
