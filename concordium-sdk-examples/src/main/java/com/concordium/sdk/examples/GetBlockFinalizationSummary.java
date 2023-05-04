package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.Credentials;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blocksummary.FinalizationData;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.SpecialOutcome;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Callable;

/**  TODO
 * Creates a {@link ClientV2} from the specified connection ("http://localhost:20001" if not specified).
 * Retrieves and prints the {@link FinalizationData} for the block {@link BlockHashInput#BEST}.
 */
@CommandLine.Command(name = "GetBlockFinalizationSummary", mixinStandardHelpOptions = true)
public class GetBlockFinalizationSummary implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20001")
    private String endpoint;

    @Override
    public Integer call() throws ClientInitializationException, MalformedURLException {
        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.builder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .credentials(new Credentials())
                .build();

        Optional<FinalizationData> res = ClientV2.from(connection).getBlockFinalizationSummary(BlockHashInput.BEST);
        res.ifPresent(System.out::println);


        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GetBlockFinalizationSummary()).execute(args);
        System.exit(exitCode);
    }
}
