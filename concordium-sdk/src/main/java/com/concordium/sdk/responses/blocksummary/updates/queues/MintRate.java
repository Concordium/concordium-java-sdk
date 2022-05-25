package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Mint rate
 * <p>
 * A base-10 floating point number representation.
 * The value is `mantissa * 10^(-exponent)`.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class MintRate {
    /**
     * The mantissa
     */
    private final long mantissa;

    /**
     * The exponent
     */
    private final int exponent;

    @JsonCreator
    MintRate(@JsonProperty("mantissa") long mantissa, @JsonProperty("exponent") int exponent) {
        this.mantissa = mantissa;
        this.exponent = exponent;
    }

    /**
     * Compute the floating point number
     * @return the mint rate.
     */
    public double getValue() {
        return mantissa * Math.pow(10, -exponent);
    }
}
