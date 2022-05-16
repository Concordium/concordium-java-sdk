package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

/**
 * Summary of the current state of consensus.
 */
@Getter
@ToString
public final class ConsensusStatus {
    /**
     * Hash of the current best block. The best block is a protocol defined
     * block that the node must use a parent block to build the chain on.
     * Note that this is subjective, in the sense that it is only the best
     * block among the blocks the node knows about.
     */
    private Hash bestBlock;
    /**
     * Hash of the genesis block.
     */
    private Hash genesisBlock;
    /**
     * Slot time of the genesis block.
     */
    private final Date genesisTime;
    /**
     * Duration of a slot.
     */
    private final int slotDuration;
    /**
     * Duration of an epoch.
     */
    private final int epochDuration;
    /**
     * Hash of the most recent finalized block.
     */
    private Hash lastFinalizedBlock;
    /**
     * The absolute height of the best block.
     * See {@link ConsensusStatus#bestBlock}
     */
    private final int bestBlockHeight;
    /**
     * The absolute height of the most recent finalized block.
     */
    private final int lastFinalizedBlockHeight;
    /**
     * The number of blocks received.
     */
    private final int blocksReceivedCount;
    /**
     * Last time a block was received and added to the nodes tree.
     * Note. The time is node local.
     */
    private final String blockLastReceivedTime;
    /**
     * Exponential moving average of block receive latency (in seconds), i.e.
     * the time between a block's nominal slot time, and the time at which is
     * received.
     */
    private final double blockReceiveLatencyEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * receiving blocks (in seconds).
     */
    private final double blockReceiveLatencyEMSD;
    /**
     * Exponential moving average of the time between receiving blocks (in
     * seconds).
     */
    private final double blockReceivePeriodEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * receiving blocks (in seconds).
     */
    private final double blockReceivePeriodEMSD;
    /**
     * Number of blocks that arrived, i.e., were added to the tree. Note that
     * in some cases this can be more than
     * {@link ConsensusStatus#blocksReceivedCount} since blocks that the node itself
     * produces count towards this, but are not received.
     */
    private final int blocksVerifiedCount;
    /**
     * The time (local time of the node) that a block last arrived, i.e., was
     * verified and added to the node's tree.
     */
    private final String blockLastArrivedTime;
    /**
     * The exponential moving average of the time between a block's nominal
     * slot time, and the time at which it is verified.
     */
    private final double blockArriveLatencyEMA;
    /**
     * Exponential moving average standard deviation of block receive latency
     * (in seconds), i.e. the time between a block's nominal slot time, and
     * the time at which is received.
     */
    private final double blockArriveLatencyEMSD;
    /**
     * Exponential moving average of the time between receiving blocks (in
     * seconds).
     */
    private final double blockArrivePeriodEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * finalizations. Will be `None` if there are no finalizations yet
     * since the node start.
     */
    private final double blockArrivePeriodEMSD;
    /**
     * Exponential moving average of the number of
     * transactions per block.
     */
    private final double transactionsPerBlockEMA;
    /**
     * Exponential moving average standard deviation of the number of
     * transactions per block.
     */
    private final double transactionsPerBlockEMSD;
    /**
     * The number of completed finalizations.
     */
    private final int finalizationCount;
    /**
     * Time at which a block last became finalized. Note that this is the local
     * time of the node at the time the block was finalized.
     */
    private final String lastFinalizedTime;
    /**
     * Exponential moving average of the time between finalizations. Will be
     * `0` if there are no finalizations yet since the node start.
     */
    private final double finalizationPeriodEMA;
    /**
     *  Exponential moving average standard deviation of the time between
     *  finalizations. Will be `0` if there are no finalizations yet
     *  since the node start.
     */
    private final double finalizationPeriodEMSD;
    /**
     * The current active protocol version.
     */
    private final ProtocolVersion protocolVersion;
    /**
     * The number of chain restarts via a protocol update. An effected
     * protocol update instruction might not change the protocol version
     * specified in the previous field, but it always increments the genesis
     * index.
     */
    private final int genesisIndex;
    /**
     * Block hash of the genesis block of current era, i.e., since the last
     * protocol update. Initially this is equal to
     * {@link ConsensusStatus#genesisBlock}.
     */
    private final String currentEraGenesisBlock;
    /**
     * ime when the current era started.
     */
    private final Date currentEraGenesisTime;

    public static ConsensusStatus fromJson(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, ConsensusStatus.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse ConsensusStatus JSON", e);
        }
    }

    @JsonCreator
    ConsensusStatus(@JsonProperty("bestBlock") String bestBlock,
                    @JsonProperty("genesisBlock") String genesisBlock,
                    @JsonProperty("genesisTime") Date genesisTime,
                    @JsonProperty("slotDuration") int slotDuration,
                    @JsonProperty("epochDuration") int epochDuration,
                    @JsonProperty("lastFinalizedBlock") String lastFinalizedBlock,
                    @JsonProperty("bestBlockHeight") int bestBlockHeight,
                    @JsonProperty("lastFinalizedBlockHeight") int lastFinalizedBlockHeight,
                    @JsonProperty("blocksReceivedCount") int blocksReceivedCount,
                    @JsonProperty("blockLastReceivedTime") String blockLastReceivedTime,
                    @JsonProperty("blockReceiveLatencyEMA") double blockReceiveLatencyEMA,
                    @JsonProperty("blockReceiveLatencyEMSD") double blockReceiveLatencyEMSD,
                    @JsonProperty("blockReceivePeriodEMA") double blockReceivePeriodEMA,
                    @JsonProperty("blockReceivePeriodEMSD") double blockReceivePeriodEMSD,
                    @JsonProperty("blocksVerifiedCount") int blocksVerifiedCount,
                    @JsonProperty("blockLastArrivedTime") String blockLastArrivedTime,
                    @JsonProperty("blockArriveLatencyEMA") double blockArriveLatencyEMA,
                    @JsonProperty("blockArriveLatencyEMSD") double blockArriveLatencyEMSD,
                    @JsonProperty("blockArrivePeriodEMA") double blockArrivePeriodEMA,
                    @JsonProperty("blockArrivePeriodEMSD") double blockArrivePeriodEMSD,
                    @JsonProperty("transactionsPerBlockEMA") double transactionsPerBlockEMA,
                    @JsonProperty("transactionsPerBlockEMSD") double transactionsPerBlockEMSD,
                    @JsonProperty("finalizationCount") int finalizationCount,
                    @JsonProperty("lastFinalizedTime") String lastFinalizedTime,
                    @JsonProperty("finalizationPeriodEMA") double finalizationPeriodEMA,
                    @JsonProperty("finalizationPeriodEMSD") double finalizationPeriodEMSD,
                    @JsonProperty("protocolVersion") ProtocolVersion protocolVersion,
                    @JsonProperty("genesisIndex") int genesisIndex,
                    @JsonProperty("currentEraGenesisBlock") String currentEraGenesisBlock,
                    @JsonProperty("currentEraGenesisTime") Date currentEraGenesisTime) {
        if (!Objects.isNull(bestBlock)) {
            this.bestBlock = Hash.from(bestBlock);
        }
        if (!Objects.isNull(genesisBlock)) {
            this.genesisBlock = Hash.from(genesisBlock);
        }
        if (!Objects.isNull(lastFinalizedBlock)) {
            this.lastFinalizedBlock = Hash.from(lastFinalizedBlock);
        }
        this.genesisTime = genesisTime;
        this.slotDuration = slotDuration;
        this.epochDuration = epochDuration;
        this.bestBlockHeight = bestBlockHeight;
        this.lastFinalizedBlockHeight = lastFinalizedBlockHeight;
        this.blocksReceivedCount = blocksReceivedCount;
        this.blockLastReceivedTime = blockLastReceivedTime;
        this.blockReceiveLatencyEMA = blockReceiveLatencyEMA;
        this.blockReceiveLatencyEMSD = blockReceiveLatencyEMSD;
        this.blockReceivePeriodEMA = blockReceivePeriodEMA;
        this.blockReceivePeriodEMSD = blockReceivePeriodEMSD;
        this.blocksVerifiedCount = blocksVerifiedCount;
        this.blockLastArrivedTime = blockLastArrivedTime;
        this.blockArriveLatencyEMA = blockArriveLatencyEMA;
        this.blockArriveLatencyEMSD = blockArriveLatencyEMSD;
        this.blockArrivePeriodEMA = blockArrivePeriodEMA;
        this.blockArrivePeriodEMSD = blockArrivePeriodEMSD;
        this.transactionsPerBlockEMA = transactionsPerBlockEMA;
        this.transactionsPerBlockEMSD = transactionsPerBlockEMSD;
        this.finalizationCount = finalizationCount;
        this.lastFinalizedTime = lastFinalizedTime;
        this.finalizationPeriodEMA = finalizationPeriodEMA;
        this.finalizationPeriodEMSD = finalizationPeriodEMSD;
        this.protocolVersion = protocolVersion;
        this.genesisIndex = genesisIndex;
        this.currentEraGenesisBlock = currentEraGenesisBlock;
        this.currentEraGenesisTime = currentEraGenesisTime;
    }
}
