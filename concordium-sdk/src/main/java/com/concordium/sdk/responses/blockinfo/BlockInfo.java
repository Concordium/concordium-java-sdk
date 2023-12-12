package com.concordium.sdk.responses.blockinfo;

import com.concordium.sdk.responses.*;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BlockInfo extends BlockIdentifier {

    /**
     * The total energy consumption of transactions in the block.
     */
    private final Integer transactionEnergyCost;
    /**
     * Identity of the baker of the block. For non-genesis blocks the value is
     * non-null.
     */
    private final BakerId blockBaker;

    /**
     * Hash of the block state at the end of the given block.
     */
    private final Hash blockStateHash;
    /**
     * Time of the block is in. In contrast to
     * {@link BlockInfo#blockArriveTime} this is an objective value, all nodes
     * agree on it.
     */
    private final Timestamp blockTime;
    /**
     * Parent block pointer.
     */
    private final Hash blockParent;
    /**
     * Time when the block was first received by the node. This can be in
     * principle quite different from the arrive time if, e.g., block execution
     * takes a long time, or the block must wait for the arrival of its parent.
     */
    private final Timestamp blockReceiveTime;
    /**
     * The genesis index for this block. This counts the number of protocol
     * updates that have preceded this block, and defines the era of the
     * block.
     */
    private final Integer genesisIndex;
    /**
     * Slot number of the slot the block is in.
     */
    private final Integer blockSlot;
    /**
     * Whether the block is finalized or not.
     */
    private final Boolean finalized;
    /**
     * The height of this block relative to the (re)genesis block of its era.
     */
    private final Integer eraBlockHeight;
    /**
     * Pointer to the last finalized block. Each block has a pointer to a
     * specific finalized block that existed at the time the block was
     * produced.
     */
    private final Hash blockLastFinalized;
    /**
     * Size of all the transactions in the block in bytes.
     */
    private final Integer transactionsSize;

    /**
     * The number of transactions in the block.
     */
    private final Integer transactionCount;
    /**
     * Time when the block was added to the node's tree. This is a subjective
     * (i.e., node specific) value.
     */
    private final Timestamp blockArriveTime;

    /**
     * The protocol version that the block
     * was baked in.
     */
    private final ProtocolVersion protocolVersion;

    /**
     * Round of the block.
     * This is non-null for protocol version 6 and above.
     */
    private final Round round;

    /**
     * Get the round of the block if the protocol version
     * is 6 or above
     *
     * @return return the round if present otherwise nothing.
     * @throws IllegalStateException if the protocol version is unrecognized.
     */
    public Optional<Round> getRound() {
        switch (ProtocolVersion.getConsensusVersion(this.protocolVersion)) {
            case V1:
                return Optional.empty();
            case V2:
                return Optional.of(this.round);
        }
        throw new IllegalStateException("Unrecognized protocol version");
    }

    /**
     * Epoch of the block.
     * This is non-null for protocol version 6 and above.
     */
    private final Epoch epoch;

    /**
     * Get the epoch of the block if the protocol version is 6 or above.
     *
     * @return the epoch if present otherwise nothing.
     * @throws IllegalStateException if the protocol version is unrecognized.
     */
    public Optional<Epoch> getEpoch() {
        switch (ProtocolVersion.getConsensusVersion(this.protocolVersion)) {
            case V1:
                return Optional.empty();
            case V2:
                return Optional.of(this.epoch);
        }
        throw new IllegalStateException("Unrecognized protocol version.");
    }

    /**
     * Hash of the Block.
     */
    public Hash getBlockHash() {
        return super.getBlockHash();
    }

    /**
     * Absolute Block Height.
     */
    public UInt64 getBlockHeight() {
        return super.getBlockHeight();
    }


}
