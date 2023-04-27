package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.responses.AccountIndex;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

/**
 * A finalizer on chain
 */
@Getter
@ToString
@Builder
public final class Finalizer {
    /**
     * The baker id
     */
    private final AccountIndex bakerId;

    /**
     * The finalizer's weight in the committee.
     */
    private final BigInteger weight;

    /**
     * Whether the finalizer's signature is present.
     */
    private final boolean signed;


    @JsonCreator
    Finalizer(@JsonProperty("bakerId") AccountIndex bakerId,
              @JsonProperty("weight") BigInteger weight,
              @JsonProperty("signed") boolean signed) {
        this.bakerId = bakerId;
        this.weight = weight;
        this.signed = signed;
    }
}
