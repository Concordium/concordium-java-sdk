package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class RootKeys {
    private final List<Key> keys;
    private final int threshold;
    private final int nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    RootKeys(@JsonProperty("keys") List<Key> keys,
             @JsonProperty("threshold") int threshold,
             @JsonProperty("nextSequenceNumber") int nextSequenceNumber,
             @JsonProperty("queue") List<Object> queue) {
        this.keys = keys;
        this.threshold = threshold;
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
