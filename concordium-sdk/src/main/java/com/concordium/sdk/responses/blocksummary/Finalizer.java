package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@ToString
public final class Finalizer {
    private final int bakerId;
    private final BigInteger weight;
    private final boolean signed;


    @JsonCreator
    Finalizer(@JsonProperty("bakerId") int bakerId,
                     @JsonProperty("weight") BigInteger weight,
                     @JsonProperty("signed") boolean signed) {
        this.bakerId = bakerId;
        this.weight = weight;
        this.signed = signed;
    }
}
