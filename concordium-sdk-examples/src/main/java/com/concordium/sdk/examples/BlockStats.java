package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.exceptions.ClientInitializationException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BlockIdentifier;
import com.concordium.sdk.responses.FinalizedBlockItemIterator;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.PaydayAccountReward;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.PaydayFoundationReward;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.PaydayPoolReward;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.SpecialOutcome;
import com.concordium.sdk.types.AbsoluteBlockHeight;
import com.google.common.collect.ImmutableList;
import lombok.val;
import lombok.var;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Traverse blocks in a given span and query statistics.
 * For each block print
 * - Block hash
 * - Block slot time
 * - Receive time of the block at the given node
 * - Arrive time of the block at the given node
 * - Difference between receive and slot times
 * - Difference between arrive and slot times
 * - Number of events associated with payday
 * - Whether the block contains a finalization record
 * - The number of transactions included in the block
 */
@CommandLine.Command(name = "BlockStats", mixinStandardHelpOptions = true)
public class BlockStats implements Callable<Integer> {

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

    @CommandLine.Option(
            names = {"--from"},
            description = "Starting time (format 2024-01-22T10:15:30+01:00). Defaults to genesis"
    )
    private Optional<String> fromString;

    @CommandLine.Option(
            names = {"--to"},
            description = "End time (format 2024-01-22T10:15:30+01:00). Defaults to the time the tool has run"
    )
    private Optional<String> toString;

    @Override
    public Integer call() throws MalformedURLException, ClientInitializationException {
        var endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .timeout(timeout)
                .build();
        var client = ClientV2.from(connection);

        AbsoluteBlockHeight start;
        if (fromString.isPresent()) {
            ZonedDateTime from = ZonedDateTime.parse(fromString.get(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            Optional<BlockInfo> b = client.findAtLowestHeight((c, query) -> {
                BlockInfo blockInfo = c.getBlockInfo(query);
                ZonedDateTime blockTime = blockInfo.getBlockTime().getZonedDateTime();
                if (from.isBefore(blockTime)) {
                    return Optional.of(blockInfo);
                } else {
                    return Optional.empty();
                }

            });
            if (b.isPresent()) {
                start = AbsoluteBlockHeight.from(b.get().getBlockHeight());
            } else {
                throw new IllegalArgumentException("Last finalized block is not after the requested start time.");
            }

        } else {
            start = AbsoluteBlockHeight.from(0);
        }

        int blockCount = 0;
        int finalizationCount = 0;
        FinalizedBlockItemIterator blocks = client.getFinalizedBlocksFrom(start);
        ZonedDateTime endTime;
        endTime = toString.map(s -> ZonedDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME)).orElseGet(ZonedDateTime::now);
        String format = "%1$-64s | %2$-29s | %3$-29s | %4$-29s | %5$-13s | %6$-12s | %7$-14s | %8$-17s | %9$-17s | %10$-30s | %11$-28s | %12$-8s\n";
        System.out.printf(format,
                "Block hash", "block time", "block receive time", "block arrive time", "receive delta", "arrive" +
                        " delta", "#payday events", "finalization data", "transaction count", "round", "epoch", "baker id");
        while (blocks.hasNext()) {
            BlockIdentifier block = blocks.next();
            BlockQuery blockQuery = BlockQuery.HASH(block.getBlockHash());
            BlockInfo blockInfo = client.getBlockInfo(blockQuery);
            ZonedDateTime blockTime = blockInfo.getBlockTime().getZonedDateTime();
            if (endTime.isBefore(blockTime)) {
                break;
            }

            int paydayBlock = 0;
            ImmutableList<SpecialOutcome> events = client.getBlockSpecialEvents(blockQuery);
            for (SpecialOutcome event: events) {
                if (event.getClass().equals(PaydayFoundationReward.class) || event.getClass().equals(PaydayAccountReward.class) || event.getClass().equals(PaydayPoolReward.class)) {
                    paydayBlock++;
                }
            }
            boolean hasFinalizationData = client.getBlockFinalizationSummary(blockQuery).isPresent();

            System.out.printf(format,
                    blockInfo.getBlockHash(),
                    blockInfo.getBlockTime(),
                    blockInfo.getBlockReceiveTime(),
                    blockInfo.getBlockArriveTime(),
                    ChronoUnit.MILLIS.between(blockInfo.getBlockTime().getZonedDateTime(), blockInfo.getBlockReceiveTime().getZonedDateTime()),
                    ChronoUnit.MILLIS.between(blockInfo.getBlockTime().getZonedDateTime(), blockInfo.getBlockArriveTime().getZonedDateTime()),
                    paydayBlock,
                    hasFinalizationData,
                    blockInfo.getTransactionCount(),
                    blockInfo.getRound().isPresent() ? blockInfo.getRound().toString() : "",
                    blockInfo.getEpoch().isPresent() ? blockInfo.getEpoch().toString() : "",
                    Objects.isNull(blockInfo.getBlockBaker()) ? "" : blockInfo.getBlockBaker().toString()

                    );

            if (hasFinalizationData) {
                finalizationCount++;
            }
            blockCount++;

        }
        blocks.drop();
        System.out.println("Block count: " + blockCount);
        System.out.println("Finalization record count: " + finalizationCount);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new BlockStats()).execute(args);
        System.exit(exitCode);
    }
}

