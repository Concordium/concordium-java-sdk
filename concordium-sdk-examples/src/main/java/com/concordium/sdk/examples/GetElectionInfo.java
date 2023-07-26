package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.responses.election.ElectionInfo;
import com.concordium.sdk.requests.BlockQuery;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetElectionInfo", mixinStandardHelpOptions = true)
public class GetElectionInfo implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException, BlockNotFoundException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ElectionInfo blockInfo = ClientV2
                .from(connection)
                .getElectionInfo(BlockQuery.BEST);

        System.out.println(blockInfo);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetElectionInfo()).execute(args);
        System.exit(exitCode);
    }
}
