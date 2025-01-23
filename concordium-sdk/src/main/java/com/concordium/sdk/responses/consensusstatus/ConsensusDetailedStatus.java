package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.AbsoluteBlockHeight;
import com.concordium.sdk.types.UInt64;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.Optional;

/**
 * Detailed current state of the consensus.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public class ConsensusDetailedStatus {

    /**
     * Hash of the genesis block.
     */
    private final Hash genesisBlock;

    /**
     * The persisted elements of the round status.
     */
    private final PersistentRoundStatus persistentRoundStatus;

    /**
     * The status of the current round.
     */
    private final RoundStatus roundStatus;

    /**
     * The number of non-finalized transactions.
     */
    private final UInt64 nonFinalizedTransactionCount;

    /**
     * The purge counter for the transaction table.
     */
    private final long transactionTablePurgeCounter;

    /**
     * Summary of the block table.
     */
    private final BlockTableSummary blockTable;

    /**
     * The live blocks organized by height after the last finalized block.
     */
    @Singular
    private final ImmutableList<BranchBlocks> branches;

    /**
     * Which bakers the node has seen legally-signed blocks with live parents from in
     * non-finalized rounds.
     */
    @Singular
    private final ImmutableList<RoundExistingBlock> roundExistingBlocks;

    /**
     * Which non-finalized rounds the node has seen quorum certificates for.
     */
    @Singular
    private final ImmutableList<RoundExistingQC> roundExistingQCs;

    /**
     * The absolute block height of the genesis block of the era.
     */
    private final AbsoluteBlockHeight genesisBlockHeight;

    /**
     * The hash of the last finalized block.
     */
    private final Hash lastFinalizedBlock;

    /**
     * The height of the last finalized block.
     */
    private final long lastFinalizedBlockHeight;

    /**
     * Unless the last finalized block is the genesis block, this should be a finalization
     * entry for the last finalized block.
     * As this includes a quorum certificate for the last finalized block, that can be used
     * to determine the epoch and round of the last finalized block.
     */
    private final RawFinalizationEntry latestFinalizationEntry;

    public Optional<RawFinalizationEntry> getLatestFinalizationEntry() {
        return Optional.ofNullable(latestFinalizationEntry);
    }

    /**
     * The bakers and finalizers for the previous, current and next epoch, relative to the last
     * finalized block.
     */
    private final EpochBakers epochBakers;

    /**
     * The timeout messages collected by the node for the current round.
     */
    private final TimeoutMessages timeoutMessages;

    public Optional<TimeoutMessages> getTimeoutMessages() {
        return Optional.ofNullable(timeoutMessages);
    }

    /**
     * If a protocol update has occurred, this is the hash of the terminal block.
     */
    private Hash terminalBlock;

    public Optional<Hash> getTerminalBlock() {
        return Optional.ofNullable(terminalBlock);
    }
}
