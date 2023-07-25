package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetConsensusStatus", mixinStandardHelpOptions = true)
public class GetConsensusStatus implements Callable<Integer> {
    @Option(
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

        ConsensusStatus consensusStatus = ClientV2
                .from(connection)
                .getConsensusInfo();

        System.out.println(consensusStatus);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetConsensusStatus()).execute(args);
        System.exit(exitCode);
    }
}
