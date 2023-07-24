package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "DumpStop", mixinStandardHelpOptions = true)
public class DumpStop implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();
        ClientV2
                .from(connection)
                .dumpStop();
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DumpStop()).execute(args);
        System.exit(exitCode);
    }
}
