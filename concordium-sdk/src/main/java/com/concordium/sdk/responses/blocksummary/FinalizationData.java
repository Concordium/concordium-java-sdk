package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Details about a finalization record included in a block
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public final class FinalizationData {

    /**
     * Block that was finalized by the finalization record.
     */
    private final String finalizationBlockPointer;
    /**
     * Index of the finalization round that finalized the block.
     */
    private final UInt64 finalizationIndex;
    /**
     * Finalization delay for the first finalization round.
     */
    private final UInt64 finalizationDelay;
    /**
     * List of all finalizers.
     */
    private final List<Finalizer> finalizers;
}
