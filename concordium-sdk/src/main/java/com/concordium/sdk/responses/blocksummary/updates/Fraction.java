package com.concordium.sdk.responses.blocksummary.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Getter
public class Fraction {
    private final BigInteger denominator;
    private final BigInteger numerator;

    @JsonCreator
    Fraction(@JsonProperty("denominator") BigInteger denominator, @JsonProperty("numerator") BigInteger numerator) {
        this.denominator = denominator;
        this.numerator = numerator;
    }
}
