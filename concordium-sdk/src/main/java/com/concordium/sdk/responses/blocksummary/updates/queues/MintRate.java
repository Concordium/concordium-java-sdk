package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class MintRate {
    private final int mantissa;
    private final int exponent;

    @JsonCreator
    MintRate(@JsonProperty("mantissa") int mantissa, @JsonProperty("exponent") int exponent) {
        this.mantissa = mantissa;
        this.exponent = exponent;
    }
}
