package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class Emergency {
    private final int threshold;
    private final List<Integer> authorizedKeys;

    @JsonCreator
    Emergency(@JsonProperty("threshold") int threshold,
              @JsonProperty("authorizedKeys") List<Integer> authorizedKeys) {
        this.threshold = threshold;
        this.authorizedKeys = authorizedKeys;
    }
}
