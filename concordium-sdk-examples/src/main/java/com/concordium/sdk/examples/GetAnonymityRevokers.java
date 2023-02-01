package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.Callable;

@Command(name = "GetAnonymityRevokers", mixinStandardHelpOptions = true)
public class GetAnonymityRevokers implements Callable<Iterator<AnonymityRevokerInfo>> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Iterator<AnonymityRevokerInfo> call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        ClientV2 clientV2 = ClientV2.from(connection);
        var arInfos = clientV2.getAnonymityRevokers(BlockHashInput.BEST);
        arInfos.forEachRemaining(System.out::println);

        return arInfos;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetAnonymityRevokers()).execute(args);
        System.exit(exitCode);
    }
}
