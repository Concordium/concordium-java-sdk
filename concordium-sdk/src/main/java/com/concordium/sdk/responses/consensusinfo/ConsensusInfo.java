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
     * TODO handle optional. milliseconds captured by Duration?
     */
    private Duration slotDuration;
    /**
     * (Current) epoch duration in milliseconds. TODO milliseconds captured by Duration?
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
     * TODO handle optional.
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
     * TODO handle optional.
     */
    private double blockReceivePeriodEMA;
    /**
     * standard deviation of exponential moving average time between receiving blocks.
     * TODO handle optional.
     */
    private double blockReceivePeriodEMSD;
    /**
     * Total number of blocks received and verified
     */
    private UInt32 blocksVerifiedCount;
    /**
     * The last time a block was verified (added to the tree).
     * TODO handle optional.
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
     * TODO handle optional.
     */
    private double blockArrivePeriodEMA;
    /**
     * Standard deviation of exponential moving average time between block arrivals.
     * TODO handle optional.
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
     * TODO handle optional.
     */
    private Timestamp lastFinalizedTime;
    /**
     * Exponential moving average time between finalizations.
     * TODO handle optional.
     */
    private double finalizationPeriodEMA;
    /**
     * Standard deviation of exponential moving average time between finalizations.
     * TODO handle optional.
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
     * @param info {@link com.concordium.grpc.v2.ConsensusInfo} returned by the GRPC V2 API.
     * @return parsed {@link ConsensusInfo}.
     */
    public static ConsensusInfo parse(com.concordium.grpc.v2.ConsensusInfo info) {
        val builder = getDefaultBuilder(info);


        return null;
    }

    /**
     * Helper method for parse method. Creates builder with always present fields set.
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
