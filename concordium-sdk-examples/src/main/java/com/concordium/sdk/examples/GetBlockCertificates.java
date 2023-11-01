package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.requests.BlockQuery;
import lombok.val;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Retrieves the BlockCertificates of the best block and prints it.
 */
@CommandLine.Command(name = "GetBlockCertificates", mixinStandardHelpOptions = true)
public class GetBlockCertificates implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @Override
    public Integer call() throws Exception {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .build();

        ClientV2 client = ClientV2.from(connection);


        val blockCertificates = client.getBlockCertificates(BlockQuery.BEST);
        System.out.println(blockCertificates);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockCertificates()).execute(args);
        System.exit(exitCode);
    }
}
