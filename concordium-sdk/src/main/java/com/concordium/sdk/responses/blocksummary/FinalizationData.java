package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

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

    @JsonCreator
    FinalizationData(@JsonProperty("finalizationBlockPointer") String finalizationBlockPointer,
                     @JsonProperty("finalizationIndex") UInt64 finalizationIndex,
                     @JsonProperty("finalizationDelay") UInt64 finalizationDelay,
                     @JsonProperty("finalizers") List<Finalizer> finalizers) {
        this.finalizationBlockPointer = finalizationBlockPointer;
        this.finalizationIndex = finalizationIndex;
        this.finalizationDelay = finalizationDelay;
        this.finalizers = finalizers;
    }
}
