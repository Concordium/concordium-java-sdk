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
    private final double mantissa;

    /**
     * The exponent
     */
    private final double exponent;

    @JsonCreator
    MintRate(@JsonProperty("mantissa") long mantissa, @JsonProperty("exponent") long exponent) {
        this.mantissa = mantissa;
        this.exponent = exponent;
    }
}
