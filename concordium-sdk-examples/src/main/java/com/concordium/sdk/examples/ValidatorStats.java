package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BakerId;
import lombok.val;
import lombok.var;
import picocli.CommandLine;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
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
            names = {"--timeout"},
            description = "GRPC request timeout in milliseconds.",
            defaultValue = "100000")
    private int timeout;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .timeout(timeout)
                .build();
        var client = ClientV2.from(connection);

        // BigInteger to avoid overflow
        BigInteger equity = BigInteger.ZERO;
        BigInteger delegated = BigInteger.ZERO;
        BigInteger effective = BigInteger.ZERO;
        var activeValidators = 0;

        val block = client.getBlockInfo(BlockQuery.LAST_FINAL).getBlockHash();
        val blockQuery = BlockQuery.HASH(block);
        val validators = client.getBakerList(blockQuery);

        StringBuilder sb = new StringBuilder();
        while (validators.hasNext()) {
            BakerId validator = validators.next();
            val pool = client.getPoolInfo(blockQuery, validator);
            val statusOptional = pool.getCurrentPaydayStatus();
            if (statusOptional.isPresent()) {
                val status = statusOptional.get();
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

                val delegators = client.getPoolDelegatorsRewardPeriod(blockQuery, validator);

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
