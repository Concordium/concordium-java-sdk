package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.TLSConfig;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.DelegatorRewardPeriodInfo;
import com.concordium.sdk.responses.poolstatus.BakerPoolStatus;
import com.concordium.sdk.responses.poolstatus.CurrentPaydayStatus;
import com.concordium.sdk.transactions.Hash;
import picocli.CommandLine;

import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * List
 * - all validators (id, own stake, stake from delegators, list of delegators and their stake)
 * - total equity
 * - total delegated stake
 * - total effective stake
 */
@CommandLine.Command(name = "ValidatorStats", mixinStandardHelpOptions = true)
public class ValidatorStats implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "http://localhost:20002")
    private String endpoint;

    @CommandLine.Option(
            names = {"--tls"},
            description = "Whether to use TLS. Uses default trust store. Use --tls-path to supply certificate"
    )
    private boolean useTLS;

    @CommandLine.Option(
            names = {"--tls-path"},
            description = "Path to the server certificate"
    )
    private Optional<String> tlsPath;

    @CommandLine.Option(
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        URL endpointUrl = new URL(this.endpoint);
        Connection.ConnectionBuilder connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .timeout(timeout);

        if (useTLS) {
            if (tlsPath.isPresent()) {
                connection = connection.useTLS(TLSConfig.from(new File(tlsPath.get())));
            } else {
                connection = connection.useTLS(TLSConfig.auto());
            }
        }

        ClientV2 client = ClientV2.from(connection.build());

        // BigInteger to avoid overflow
        BigInteger equity = BigInteger.ZERO;
        BigInteger delegated = BigInteger.ZERO;
        BigInteger effective = BigInteger.ZERO;
        int activeValidators = 0;

        Hash block = client.getBlockInfo(BlockQuery.LAST_FINAL).getBlockHash();
        BlockQuery blockQuery = BlockQuery.HASH(block);
        Iterator<BakerId> validators = client.getBakerList(blockQuery);

        StringBuilder sb = new StringBuilder();
        while (validators.hasNext()) {
            BakerId validator = validators.next();
            BakerPoolStatus pool = client.getPoolInfo(blockQuery, validator);
            Optional<CurrentPaydayStatus> statusOptional = pool.getCurrentPaydayStatus();
            if (statusOptional.isPresent()) {
                CurrentPaydayStatus status = statusOptional.get();
                long equityVal = status.getBakerEquityCapital().getValue().getValue();
                long delegatedVal = status.getDelegatedCapital().getValue().getValue();
                long effectiveVal = status.getEffectiveStake().getValue().getValue();

                activeValidators++;
                equity = equity.add(BigInteger.valueOf(equityVal));
                delegated = delegated.add(BigInteger.valueOf(delegatedVal));
                effective = effective.add(BigInteger.valueOf(effectiveVal));

                // Add info about this specific validator
                sb.append("Validator ").append(validator).append(": own stake = ").append(equityVal)
                        .append(" micro CCD, from delegators = ").append(delegatedVal).append(" micro CCD");

                Iterator<DelegatorRewardPeriodInfo> delegators = client.getPoolDelegatorsRewardPeriod(blockQuery, validator);

                // Add info about delegators if present
                if (delegators.hasNext()) {
                    sb.append("\n Delegators: \n");
                    delegators.forEachRemaining(
                            d -> sb.append("\t")
                                    .append("Account: ").append(d.getAccount())
                                    .append(", stake: ").append(d.getStake())
                                    .append("\n")
                    );
                }

                sb.append("\n");

            }
        }
        sb.append("\nThere are ").append(activeValidators).append(" active validators\n")
            .append("Total effective stake is ").append(effective).append(" micro CCD\n")
            .append("Total equity capital is ").append(equity).append(" micro CCD\n")
            .append("Total delegated stake is ").append(delegated).append(" micro CCD\n");
        System.out.println(sb);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ValidatorStats()).execute(args);
        System.exit(exitCode);
    }
}
