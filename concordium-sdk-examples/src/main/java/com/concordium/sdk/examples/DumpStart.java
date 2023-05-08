package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.dumpstart.DumpRequest;
import picocli.CommandLine;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "DumpStart", mixinStandardHelpOptions = true)
public class DumpStart implements Callable<Integer> {

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
        Path path = Paths.get("./myDump");
        DumpRequest dumpRequest = DumpRequest.builder()
                .path(path)
                .raw(true).build();
        ClientV2
                .from(connection)
                .dumpStart(dumpRequest);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DumpStart()).execute(args);
        System.exit(exitCode);
    }
}
