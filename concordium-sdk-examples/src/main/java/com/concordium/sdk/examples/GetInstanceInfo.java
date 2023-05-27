package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.intanceinfo.InstanceInfo;
import com.concordium.sdk.types.ContractAddress;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Gets the Contract Instance Information at the {@link BlockHashInput#BEST} block.
 */
@Command(name = "GetInstanceInfo", mixinStandardHelpOptions = true)
public class GetInstanceInfo implements Callable<Integer> {
    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20000")
    private String endpoint;

    @Option(names = {"--index"}, description = "Index", defaultValue = "2000")
    private long index;

    @Option(names = {"--subindex"}, description = "Sub Index", defaultValue = "0")
    private long subindex;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetInstanceInfo()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        InstanceInfo instanceINfo = ClientV2
                .from(connection)
                .getInstanceInfo(BlockHashInput.BEST, ContractAddress.from(index, subindex));

        System.out.println(instanceINfo);

        return 0;
    }
}
