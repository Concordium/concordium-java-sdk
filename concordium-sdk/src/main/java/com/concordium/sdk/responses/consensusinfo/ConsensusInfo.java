package com.concordium.sdk.responses.consensusinfo;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.time.Duration;

/**
 * Information about the current state of consensus.
 * Response for getConsensusInfo.
 */
@Getter
@EqualsAndHashCode
@Builder
@ToString
public class ConsensusInfo {

    /**
     * Hash of the current best block.
     */
    private Hash bestBlock;
    /**
     * Hash of the (original) genesis block.
     */
    private Hash genesisBlock;
    /**
     * Time of the (original) genesis block.
     */
    private Timestamp genesisTime;
    /**
     * (Current) slot duration in milliseconds. Present only in protocol versions 1-5.
     * TODO handle optional.
     */
    private Duration slotDuration;
    /**
     * (Current) epoch duration in milliseconds.
     */
    private Duration epochDuration;
    /**
     * Hash of the last finalized block.
     */
    private Hash lastFinalizedBlock;
    /**
     * Absolute height of the best block.
     */
    private UInt64 bestBlockHeight;
    /**
     * Absolute height of the last finalized block.
     */
    private UInt64 lastFinalizedBlockHeight;
    /**
     * Total number of blocks received.
     */
    private UInt32 blocksReceivedCount;
    /**
     * The last time a block was received.
     * Note, may be null if TODO when is it null?
     */
    private Timestamp blockLastReceivedTime;
    /**
     * Exponential moving average latency between a block's slot time and received time.
     */
    private double blockReceiveLatencyEMA;
    /**
     * Standard deviation of exponential moving average latency between a block's slot time and received time.
     */
    private double blockReceiveLatencyEMSD;
    /**
     * Exponential moving average time between receiving blocks.
     * Note, may be null if TODO when is it null?
     */
    private double blockReceivePeriodEMA;
    /**
     * standard deviation of exponential moving average time between receiving blocks.
     * Note, may be null if TODO when is it null?
     */
    private double blockReceivePeriodEMSD;
    /**
     * Total number of blocks received and verified
     */
    private UInt32 blocksVerifiedCount;
    /**
     * The last time a block was verified (added to the tree).
     * Note, may be null if TODO when is it null?
     */
    private Timestamp blockLastArrivedTime;
    /**
     * Exponential moving average latency between a block's slot time and its arrival.
     */
    private double blockArriveLatencyEMA;
    /**
     * Standard deviation of exponential moving average latency between a block's slot time and its arrival.
     */
    private double blockArriveLatencyEMSD;
    /**
     * Exponential moving average time between block arrivals.
     * Note, may be null if TODO when is it null?
     */
    private double blockArrivePeriodEMA;
    /**
     * Standard deviation of exponential moving average time between block arrivals.
     * Note, may be null if TODO when is it null?
     */
    private double blockArrivePeriodEMSD;
    /**
     * Exponential moving average number of transactions per block.
     */
    private double transactionsPerBlockEMA;
    /**
     * Standard deviation of exponential moving average number of transactions per block.
     */
    private double transactionsPerBlockEMSD;
    /**
     * Number of finalizations.
     */
    private UInt32 finalizationCount;
    /**
     * Time of last verified finalization.
     * Note, may be null if TODO when is it null?
     */
    private Timestamp lastFinalizedTime;
    /**
     * Exponential moving average time between finalizations.
     * Note, may be null if TODO when is it null?
     */
    private double finalizationPeriodEMA;
    /**
     * Standard deviation of exponential moving average time between finalizations.
     * Note, may be null if TODO when is it null?
     */
    private double finalizationPeriodEMSD;
    /**
     * Currently active protocol version.
     */
    private ProtocolVersion protocolVersion;
    /**
     * The number of chain restarts via a protocol update. A completed
     * protocol update instruction might not change the protocol version
     * specified in the previous field, but it always increments the genesis
     * index.
     */
    private UInt32 genesisIndex;
    /**
     * Block hash of the genesis block of current era, i.e., since the last protocol update.
     * Initially this is equal to 'genesis_block'.
     */
    private Hash currentEraGenesisBlock;
    /**
     * Time when the current era started.
     */
    private Timestamp currentEraGenesisTime;
    /**
     * The current duration to wait before a round times out. Present from protocol version 6.
     * TODO handle optional.
     */
    private Duration currentTimeoutDuration;
    /**
     * The current round. Present from protocol version 6.
     * TODO handle optional.
     */
    private UInt64 currentRound;
    /**
     * The current epoch. Present from protocol version 6.
     * TODO handle optional.
     */
    private UInt64 currentEpoch;
    /**
     * The first block in the epoch with timestamp at least this is considered to be the trigger block
     * for the epoch transition. Present from protocol version 6.
     * TODO handle optional.
     */
    private Timestamp triggerBlockTime;

    /**
     * Parses {@link com.concordium.grpc.v2.ConsensusInfo} to {@link ConsensusInfo}.
     *
     * @param info {@link com.concordium.grpc.v2.ConsensusInfo} returned by the GRPC V2 API.
     * @return parsed {@link ConsensusInfo}.
     */
    public static ConsensusInfo parse(com.concordium.grpc.v2.ConsensusInfo info) {
        var builder = getDefaultBuilder(info);
        addOptionalsIfPresent(info, builder);
        return builder.build();
    }

    /**
     * Helper method for parse method. Adds optional fields to builder if present.
     *
     * @param info    {@link com.concordium.grpc.v2.ConsensusInfo} returned by the GRPC V2 API.
     * @param builder {@link ConsensusInfoBuilder}.
     */
    private static void addOptionalsIfPresent(com.concordium.grpc.v2.ConsensusInfo info, ConsensusInfoBuilder builder) {
        if (info.hasSlotDuration()) {
            builder.slotDuration(Duration.ofMillis(info.getSlotDuration().getValue()));
        }
        if (info.hasBlockLastReceivedTime()) {
            builder.blockLastReceivedTime(Timestamp.newMillis(info.getBlockLastReceivedTime().getValue()));
        }
        if (info.hasBlockReceivePeriodEma()) {
            builder.blockReceivePeriodEMA(info.getBlockReceivePeriodEma());
        }
        if (info.hasBlockReceivePeriodEmsd()) {
            builder.blockReceivePeriodEMSD(info.getBlockReceivePeriodEmsd());
        }
        if (info.hasBlockLastArrivedTime()) {
            builder.blockLastArrivedTime(Timestamp.newMillis(info.getBlockLastArrivedTime().getValue()));
        }
        if (info.hasBlockArrivePeriodEma()) {
            builder.blockArrivePeriodEMA(info.getBlockArrivePeriodEma());
        }
        if (info.hasBlockArrivePeriodEmsd()) {
            builder.blockArrivePeriodEMSD(info.getBlockArrivePeriodEmsd());
        }
        if (info.hasLastFinalizedTime()) {
            builder.lastFinalizedTime(Timestamp.newMillis(info.getLastFinalizedTime().getValue()));
        }
        if (info.hasFinalizationPeriodEma()) {
            builder.finalizationPeriodEMA(info.getFinalizationPeriodEma());
        }
        if (info.hasFinalizationPeriodEmsd()) {
            builder.finalizationPeriodEMSD(info.getFinalizationPeriodEmsd());
        }
        if (info.hasCurrentTimeoutDuration()) {
            builder.currentTimeoutDuration(Duration.ofMillis(info.getCurrentTimeoutDuration().getValue()));
        }
        if (info.hasCurrentRound()) {
            builder.currentRound(UInt64.from(info.getCurrentRound().getValue()));
        }
        if (info.hasCurrentEpoch()) {
            builder.currentEpoch(UInt64.from(info.getCurrentEpoch().getValue()));
        }
        if (info.hasTriggerBlockTime()) {
            builder.triggerBlockTime(Timestamp.newMillis(info.getTriggerBlockTime().getValue()));
        }
    }

    /**
     * Helper method for parse method. Creates builder with always present fields set.
     *
     * @param info {@link com.concordium.grpc.v2.ConsensusInfo} returned by the GRPC V2 API.
     * @return {@link ConsensusInfoBuilder} with always present fields set.
     */
    private static ConsensusInfoBuilder getDefaultBuilder(com.concordium.grpc.v2.ConsensusInfo info) {
        return ConsensusInfo.builder()
                .bestBlock(Hash.from(info.getBestBlock().getValue().toByteArray()))
                .genesisBlock(Hash.from(info.getGenesisBlock().getValue().toByteArray()))
                .genesisTime(Timestamp.newMillis(info.getGenesisTime().getValue()))
                .epochDuration(Duration.ofMillis(info.getEpochDuration().getValue()))
                .lastFinalizedBlock(Hash.from(info.getLastFinalizedBlock().getValue().toByteArray()))
                .bestBlockHeight(UInt64.from(info.getBestBlockHeight().getValue()))
                .lastFinalizedBlockHeight(UInt64.from(info.getLastFinalizedBlockHeight().getValue()))
                .blocksReceivedCount(UInt32.from(info.getBlocksReceivedCount()))
                .blockReceiveLatencyEMA(info.getBlockReceiveLatencyEma())
                .blockReceiveLatencyEMSD(info.getBlockReceiveLatencyEmsd())
                .blocksVerifiedCount(UInt32.from(info.getBlocksVerifiedCount()))
                .blockArriveLatencyEMA(info.getBlockArriveLatencyEma())
                .blockArriveLatencyEMSD(info.getBlockArriveLatencyEmsd())
                .transactionsPerBlockEMA(info.getTransactionsPerBlockEma())
                .transactionsPerBlockEMSD(info.getTransactionsPerBlockEmsd())
                .finalizationCount(UInt32.from(info.getFinalizationCount()))
                .protocolVersion(ProtocolVersion.parse(info.getProtocolVersion()))
                .genesisIndex(UInt32.from(info.getGenesisIndex().getValue()))
                .currentEraGenesisBlock(Hash.from(info.getCurrentEraGenesisBlock().getValue().toByteArray()))
                .currentEraGenesisTime(Timestamp.newMillis(info.getCurrentEraGenesisTime().getValue()));
    }

}
