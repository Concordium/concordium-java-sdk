package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

/**
 * Summary of the current state of consensus.
 */
@Getter
@ToString
@Jacksonized
@Builder
public final class ConsensusStatus {
    /**
     * Hash of the current best block. The best block is a protocol defined
     * block that the node must use a parent block to build the chain on.
     * Note that this is subjective, in the sense that it is only the best
     * block among the blocks the node knows about.
     */
    @JsonProperty("bestBlock")
    private Hash bestBlock;
    /**
     * Hash of the genesis block.
     */
    @JsonProperty("genesisBlock")
    private Hash genesisBlock;
    /**
     * Slot time of the genesis block.
     */
    @JsonProperty("genesisTime")
    private final Date genesisTime;
    /**
     * Duration of a slot.
     */
    @JsonProperty("slotDuration")
    private final java.time.Duration slotDuration;
    /**
     * Duration of an epoch.
     */
    @JsonProperty("epochDuration")
    private final java.time.Duration epochDuration;
    /**
     * Hash of the most recent finalized block.
     */
    @JsonProperty("lastFinalizedBlock")
    private Hash lastFinalizedBlock;
    /**
     * The absolute height of the best block.
     * See {@link ConsensusStatus#bestBlock}
     */
    @JsonProperty("bestBlockHeight")
    private final long bestBlockHeight;
    /**
     * The absolute height of the most recent finalized block.
     */
    @JsonProperty("lastFinalizedBlockHeight")
    private final long lastFinalizedBlockHeight;
    /**
     * The number of blocks received.
     */
    @JsonProperty("blocksReceivedCount")
    private final int blocksReceivedCount;
    /**
     * Last time a block was received and added to the nodes tree.
     * Note. The time is node local.
     */
    @JsonProperty("blockLastReceivedTime")
    private final String blockLastReceivedTime;
    /**
     * Exponential moving average of block receive latency (in seconds), i.e.
     * the time between a block's nominal slot time, and the time at which is
     * received.
     */
    @JsonProperty("blockReceiveLatencyEMA")
    private final double blockReceiveLatencyEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * receiving blocks (in seconds).
     */
    @JsonProperty("blockReceiveLatencyEMSD")
    private final double blockReceiveLatencyEMSD;
    /**
     * Exponential moving average of the time between receiving blocks (in
     * seconds).
     */
    @JsonProperty("blockReceivePeriodEMA")
    private final double blockReceivePeriodEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * receiving blocks (in seconds).
     */
    @JsonProperty("blockReceivePeriodEMSD")
    private final double blockReceivePeriodEMSD;
    /**
     * Number of blocks that arrived, i.e., were added to the tree. Note that
     * in some cases this can be more than
     * {@link ConsensusStatus#blocksReceivedCount} since blocks that the node itself
     * produces count towards this, but are not received.
     */
    @JsonProperty("blocksVerifiedCount")
    private final int blocksVerifiedCount;
    /**
     * The time (local time of the node) that a block last arrived, i.e., was
     * verified and added to the node's tree.
     */
    @JsonProperty("blockLastArrivedTime")
    private final String blockLastArrivedTime;
    /**
     * The exponential moving average of the time between a block's nominal
     * slot time, and the time at which it is verified.
     */
    @JsonProperty("blockArriveLatencyEMA")
    private final double blockArriveLatencyEMA;
    /**
     * Exponential moving average standard deviation of block receive latency
     * (in seconds), i.e. the time between a block's nominal slot time, and
     * the time at which is received.
     */
    @JsonProperty("blockArriveLatencyEMSD")
    private final double blockArriveLatencyEMSD;
    /**
     * Exponential moving average of the time between receiving blocks (in
     * seconds).
     */
    @JsonProperty("blockArrivePeriodEMA")
    private final double blockArrivePeriodEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * finalizations. Will be `None` if there are no finalizations yet
     * since the node start.
     */
    @JsonProperty("blockArrivePeriodEMSD")
    private final double blockArrivePeriodEMSD;
    /**
     * Exponential moving average of the number of
     * transactions per block.
     */
    @JsonProperty("transactionsPerBlockEMA")
    private final double transactionsPerBlockEMA;
    /**
     * Exponential moving average standard deviation of the number of
     * transactions per block.
     */
    @JsonProperty("transactionsPerBlockEMSD")
    private final double transactionsPerBlockEMSD;
    /**
     * The number of completed finalizations.
     */
    @JsonProperty("finalizationCount")
    private final int finalizationCount;
    /**
     * Time at which a block last became finalized. Note that this is the local
     * time of the node at the time the block was finalized.
     */
    @JsonProperty("lastFinalizedTime")
    private final String lastFinalizedTime;
    /**
     * Exponential moving average of the time between finalizations. Will be
     * `0` if there are no finalizations yet since the node start.
     */
    @JsonProperty("finalizationPeriodEMA")
    private final double finalizationPeriodEMA;
    /**
     *  Exponential moving average standard deviation of the time between
     *  finalizations. Will be `0` if there are no finalizations yet
     *  since the node start.
     */
    @JsonProperty("finalizationPeriodEMSD")
    private final double finalizationPeriodEMSD;
    /**
     * The current active protocol version.
     */
    @JsonProperty("protocolVersion")
    private final ProtocolVersion protocolVersion;
    /**
     * The number of chain restarts via a protocol update. An effected
     * protocol update instruction might not change the protocol version
     * specified in the previous field, but it always increments the genesis
     * index.
     */
    @JsonProperty("genesisIndex")
    private final int genesisIndex;
    /**
     * Block hash of the genesis block of current era, i.e., since the last
     * protocol update. Initially this is equal to
     * {@link ConsensusStatus#genesisBlock}.
     */
    @JsonProperty("currentEraGenesisBlock")
    private final String currentEraGenesisBlock;
    /**
     * Time when the current era started.
     */
    @JsonProperty("currentEraGenesisTime")
    private final Date currentEraGenesisTime;

    public static ConsensusStatus fromJson(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, ConsensusStatus.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse ConsensusStatus JSON", e);
        }
    }
}
