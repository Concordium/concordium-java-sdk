package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class Fraction {
    private final int denominator;
    private final int numerator;

    @JsonCreator
    Fraction(@JsonProperty("denominator") int denominator, @JsonProperty("numerator") int numerator) {
        this.denominator = denominator;
        this.numerator = numerator;
    }
}
