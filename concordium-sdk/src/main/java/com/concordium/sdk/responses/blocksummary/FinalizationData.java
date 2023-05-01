package com.concordium.sdk.responses.blocksummary;

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
    private final int finalizationIndex;
    /**
     * Finalization delay for the first finalization round.
     */
    private final int finalizationDelay;
    /**
     * List of all finalizers.
     */
    private final List<Finalizer> finalizers;

    @JsonCreator
    FinalizationData(@JsonProperty("finalizationBlockPointer") String finalizationBlockPointer,
                     @JsonProperty("finalizationIndex") int finalizationIndex,
                     @JsonProperty("finalizationDelay") int finalizationDelay,
                     @JsonProperty("finalizers") List<Finalizer> finalizers) {
        this.finalizationBlockPointer = finalizationBlockPointer;
        this.finalizationIndex = finalizationIndex;
        this.finalizationDelay = finalizationDelay;
        this.finalizers = finalizers;
    }
}
