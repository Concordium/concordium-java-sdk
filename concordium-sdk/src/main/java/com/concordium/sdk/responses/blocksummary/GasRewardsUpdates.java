package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class GasRewardsUpdates {
    private final Nonce nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    GasRewardsUpdates(@JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
                      @JsonProperty("queue") List<Object> queue) {
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
