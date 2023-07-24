package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.branch.Branch;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetBranches", mixinStandardHelpOptions = true)
public class GetBranches implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException, BlockNotFoundException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        Branch root = ClientV2
                .from(connection)
                .getBranches();

        System.out.println(root);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBranches()).execute(args);
        System.exit(exitCode);
    }
}
