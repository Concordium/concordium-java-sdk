package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

/**
 * A finalizer on chain
 */
@Getter
@ToString
public final class Finalizer {
    /**
     * The baker id
     */
    private final int bakerId;

    /**
     * The finalizer's relative weight in the committee.
     */
    private final BigInteger weight;

    /**
     * Whether the finalizer's signature is present.
     */
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
