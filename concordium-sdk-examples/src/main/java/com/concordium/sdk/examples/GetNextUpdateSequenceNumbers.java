package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.NextUpdateSequenceNumbers;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Gets the chain parameters Update Sequence Numbers at the end of {@link BlockHashInput#BEST} block.
 */
@Command(name = "GetNextUpdateSequenceNumbers", mixinStandardHelpOptions = true)
public class GetNextUpdateSequenceNumbers implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetNextUpdateSequenceNumbers()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        NextUpdateSequenceNumbers instanceINfo = ClientV2
                .from(connection)
                .getNextUpdateSequenceNumbers(BlockHashInput.BEST);

        System.out.println(instanceINfo);

        return 0;
    }
}
