package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class MintDistribution {
    private final double mintPerSlot;
    private final double bakingReward;
    private final double finalizationReward;
    private final int threshold;
    private final List<Integer> authorizedKeys;
    private final int nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    MintDistribution(@JsonProperty("mintPerSlot") double mintPerSlot,
                     @JsonProperty("bakingReward") double bakingReward,
                     @JsonProperty("finalizationReward") double finalizationReward,
                     @JsonProperty("threshold") int threshold,
                     @JsonProperty("authorizedKeys") List<Integer> authorizedKeys,
                     @JsonProperty("nextSequenceNumber") int nextSequenceNumber,
                     @JsonProperty("queue") List<Object> queue) {
        this.mintPerSlot = mintPerSlot;
        this.bakingReward = bakingReward;
        this.finalizationReward = finalizationReward;
        this.threshold = threshold;
        this.authorizedKeys = authorizedKeys;
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
