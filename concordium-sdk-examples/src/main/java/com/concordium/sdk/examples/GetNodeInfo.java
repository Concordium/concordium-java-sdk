package com.concordium.sdk.examples;

import com.concordium.grpc.v2.Empty;
import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.responses.nodeinfo.NodeInfo;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URL;
import java.util.concurrent.Callable;

@Command(name = "GetNodeInfo", mixinStandardHelpOptions = true)
public class GetNodeInfo implements Callable<Integer> {

    @Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws Exception {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        ClientV2 client = ClientV2.from(connection);


        NodeInfo nodeInfo = client.getNodeInfo();
        System.out.println(nodeInfo);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetNodeInfo()).execute(args);
        System.exit(exitCode);
    }
}
