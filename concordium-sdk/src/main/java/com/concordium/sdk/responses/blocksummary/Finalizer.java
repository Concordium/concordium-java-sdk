package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Finalizer {
    private final int bakerId;
    private final Object weight;
    private final boolean signed;


    @JsonCreator
    Finalizer(@JsonProperty("bakerId") int bakerId,
                     @JsonProperty("weight") Object weight,
                     @JsonProperty("signed") boolean signed) {
        this.bakerId = bakerId;
        this.weight = weight;
        this.signed = signed;
    }
}
