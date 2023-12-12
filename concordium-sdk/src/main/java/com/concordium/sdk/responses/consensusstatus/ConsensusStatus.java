package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.util.Optional;

/**
 * Summary of the current state of consensus.
 */
@ToString(doNotUseGetters = true)
@Builder
@EqualsAndHashCode
public final class ConsensusStatus {
    /**
     * Hash of the current best block. The best block is a protocol defined
     * block that the node must use a parent block to build the chain on.
     * Note that this is subjective, in the sense that it is only the best
     * block among the blocks the node knows about.
     */
    @Getter
    private Hash bestBlock;

    /**
     * Hash of the genesis block.
     */
    @Getter
    private Hash genesisBlock;

    /**
     * Time of the genesis block.
     */
    @Getter
    private final Timestamp genesisTime;

    /**
     * Duration of a slot if available.
     * Slots exist for protocol versions 1-5.
     * From protocol version 6 and onwards the consensus protocol
     * uses the concept of rounds.
     */
    private final Duration slotDuration;

    /**
     * Returns the slot duration if the protocol supports it.
     * This is supported in protocol versions 1-5.
     *
     * @return the slot duration
     * @throws IllegalStateException if the protocol is unrecognized.
     */
    public Optional<Duration> getSlotDuration() {
        switch (ProtocolVersion.getConsensusVersion(this.protocolVersion)) {
            case V1:
                return Optional.of(this.slotDuration);
            case V2:
                return Optional.empty();
        }
        throw new IllegalStateException("Unrecognized protocol version.");
    }

    /**
     * Duration of an epoch.
     */
    @Getter
    private final java.time.Duration epochDuration;

    /**
     * Hash of the most recent finalized block.
     */
    @Getter
    private Hash lastFinalizedBlock;

    /**
     * The absolute height of the best block.
     * See {@link ConsensusStatus#bestBlock}
     */
    @Getter
    private final long bestBlockHeight;

    /**
     * The absolute height of the most recent finalized block.
     */
    @Getter
    private final long lastFinalizedBlockHeight;

    /**
     * The number of blocks received.
     */
    @Getter
    private final int blocksReceivedCount;

    /**
     * Last time a block was received and added to the nodes tree.
     * Note. The time is node local.
     */
    @Getter
    private Timestamp blockLastReceivedTime;

    /**
     * Exponential moving average of block receive latency (in seconds), i.e.
     * the time between a block's nominal time, and the time at which is
     * received.
     */
    @Getter
    private final double blockReceiveLatencyEMA;

    /**
     * Exponential moving average standard deviation of the time between
     * receiving blocks (in seconds).
     */
    @Getter
    private final double blockReceiveLatencyEMSD;

    /**
     * Exponential moving average of the time between receiving blocks (in
     * seconds).
     */
    @Getter
    private final double blockReceivePeriodEMA;

    /**
     * Exponential moving average standard deviation of the time between
     * receiving blocks (in seconds).
     */
    @Getter
    private final double blockReceivePeriodEMSD;

    /**
     * Number of blocks that arrived, i.e., were added to the tree. Note that
     * in some cases this can be more than
     * {@link ConsensusStatus#blocksReceivedCount} since blocks that the node itself
     * produces count towards this, but are not received.
     */
    @Getter
    private final int blocksVerifiedCount;

    /**
     * The time (local time of the node) that a block last arrived, i.e., was
     * verified and added to the node's tree.
     */
    @Getter
    private Timestamp blockLastArrivedTime;

    /**
     * The exponential moving average of the time between a block's nominal
     * time, and the time at which it is verified.
     */
    @Getter
    private final double blockArriveLatencyEMA;
    /**
     * Exponential moving average standard deviation of block receive latency
     * (in seconds), i.e. the time between a block's nominal time, and
     * the time at which is received.
     */
    @Getter
    private final double blockArriveLatencyEMSD;
    /**
     * Exponential moving average of the time between receiving blocks (in
     * seconds).
     */
    @Getter
    private final double blockArrivePeriodEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * finalizations. Will be `None` if there are no finalizations yet
     * since the node start.
     */
    @Getter
    private final double blockArrivePeriodEMSD;
    /**
     * Exponential moving average of the number of
     * transactions per block.
     */
    @Getter
    private final double transactionsPerBlockEMA;
    /**
     * Exponential moving average standard deviation of the number of
     * transactions per block.
     */
    @Getter
    private final double transactionsPerBlockEMSD;
    /**
     * The number of completed finalizations.
     */
    @Getter
    private final int finalizationCount;
    /**
     * Time at which a block last became finalized. Note that this is the local
     * time of the node at the time the block was finalized.
     */
    @Getter
    private Timestamp lastFinalizedTime;

    /**
     * Exponential moving average of the time between finalizations. Will be
     * `0` if there are no finalizations yet since the node start.
     */
    @Getter
    private final double finalizationPeriodEMA;
    /**
     * Exponential moving average standard deviation of the time between
     * finalizations. Will be `0` if there are no finalizations yet
     * since the node start.
     */
    @Getter
    private final double finalizationPeriodEMSD;
    /**
     * The current active protocol version.
     */
    @Getter
    private final ProtocolVersion protocolVersion;
    /**
     * The number of chain restarts via a protocol update. An effected
     * protocol update instruction might not change the protocol version
     * specified in the previous field, but it always increments the genesis
     * index.
     */
    @Getter
    private final int genesisIndex;
    /**
     * Block hash of the genesis block of current era, i.e., since the last
     * protocol update. Initially this is equal to
     * {@link ConsensusStatus#genesisBlock}.
     */
    @Getter
    private final Hash currentEraGenesisBlock;
    /**
     * Time when the current era started.
     */
    @Getter
    private Timestamp currentEraGenesisTime;

    /**
     * The current duration to wait before a round times out.
     * Note that this is only present from protocol version 6.
     */
    private final Duration currentTimeoutDuration;

    /**
     * Gets the current timeout duration if the protocol supports it.
     * This is supported in protocol version 6 and onwards.
     *
     * @return the current timeout duration if supported.
     * @throws IllegalStateException if the protocol is unrecognized
     */
    public Optional<Duration> getCurrentTimeoutDuration() {
        switch (ProtocolVersion.getConsensusVersion(this.protocolVersion)) {
            case V1:
                return Optional.empty();
            case V2:
                return Optional.of(this.currentTimeoutDuration);
        }
        throw new IllegalStateException("Unrecognized protocol version");
    }

    /**
     * The current round from the perspective of the node queried.
     */
    private final Round currentRound;

    /**
     * Get the current round if the current protocol version is version 6 or above,
     * otherwise return nothing.
     *
     * @return the current round wrapped in an optional implying whether it's supported or not.
     * @throws IllegalStateException if the protocol version is unrecognized.
     */
    public Optional<Round> getCurrentRound() {
        switch (ProtocolVersion.getConsensusVersion(this.protocolVersion)) {
            case V1:
                return Optional.empty();
            case V2:
                return Optional.of(this.currentRound);
        }
        throw new IllegalStateException("Unrecognized protocol version");
    }

    /**
     * The current epoch from the perspective of the node queried.
     * This is only available from protocol version 6 and onwards.
     */
    private final Epoch currentEpoch;

    /**
     * Get the current epoch if the protocol version is 6 or more,
     * otherwise this yields nothing.
     *
     * @return the current epoch.
     * @throws IllegalStateException if the protocol version is unrecognized.
     */
    public Optional<Epoch> getCurrentEpoch() {
        switch (ProtocolVersion.getConsensusVersion(this.protocolVersion)) {
            case V1:
                return Optional.empty();
            case V2:
                return Optional.of(this.currentEpoch);
        }
        throw new IllegalStateException("Unrecognized protocol version");
    }

    /**
     * The nominal time of the trigger block.
     * Whenever the trigger block is finalized then the consensus protocol
     * will progress to a new {@link Epoch}.
     */
    private final Timestamp triggerBlockTime;

    /**
     * Get the trigger block time of if present.
     *
     * @return the trigger block time if the protocol version is 6 or more.
     * @throws IllegalStateException if the protocol version is unrecognized.
     */
    public Optional<Timestamp> getTriggerBlockTime() {
        switch (ProtocolVersion.getConsensusVersion(this.protocolVersion)) {
            case V1:
                return Optional.empty();
            case V2:
                return Optional.of(triggerBlockTime);
        }
        throw new IllegalStateException("Unrecognized protocol version");
    }

}
