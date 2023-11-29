package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import lombok.val;
import lombok.var;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetBlockChainParameters", mixinStandardHelpOptions = true)
public class GetCryptographicParameters implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);

        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        val client = ClientV2.from(connection);
        CryptographicParameters getBlockItemStatusFailure = client
                .getCryptographicParameters(BlockQuery.BEST);

        System.out.println(getBlockItemStatusFailure);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetCryptographicParameters()).execute(args);
        System.exit(exitCode);
    }
}
