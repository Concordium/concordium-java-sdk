package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class GasRewards2 {
    private final int nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    GasRewards2(@JsonProperty("nextSequenceNumber") int nextSequenceNumber,
                @JsonProperty("queue") List<Object> queue) {
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
