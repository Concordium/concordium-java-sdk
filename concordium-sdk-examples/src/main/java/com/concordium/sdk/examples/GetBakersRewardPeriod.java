package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.bakersrewardperiod.BakerRewardPeriodInfo;
import lombok.val;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Calls {@link ClientV2#getBakersRewardPeriod(BlockQuery)}
 * retrieving a list of {@link BakerRewardPeriodInfo} about the bakers in the reward period of the block.
 * Prints the result to the terminal.
 */
@CommandLine.Command(name = "GetBakersRewardPeriod", mixinStandardHelpOptions = true)
public class GetBakersRewardPeriod implements Callable<Integer> {

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


        val bakerInfo = client.getBakersRewardPeriod(BlockQuery.BEST);
        bakerInfo.forEach(System.out::println);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBakersRewardPeriod()).execute(args);
        System.exit(exitCode);
    }
}
