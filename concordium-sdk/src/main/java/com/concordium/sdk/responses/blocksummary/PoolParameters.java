package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class PoolParameters {
    private final int threshold;
    private final List<Integer> authorizedKeys;
    private final List<Object> queue;

    @JsonCreator
    PoolParameters(
            @JsonProperty("threshold") int threshold,
            @JsonProperty("authorizedKeys") List<Integer> authorizedKeys,
            @JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
            @JsonProperty("queue") List<Object> queue) {
        this.threshold = threshold;
        this.authorizedKeys = authorizedKeys;
        this.queue = queue;
    }
}
