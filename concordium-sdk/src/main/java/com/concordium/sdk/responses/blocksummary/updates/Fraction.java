package com.concordium.sdk.responses.blocksummary.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.math.BigInteger;

/**
 * A fraction
 */
@EqualsAndHashCode
@Getter
public class Fraction {
    private final BigInteger denominator;
    private final BigInteger numerator;

    @JsonCreator
    public Fraction(@JsonProperty("denominator") BigInteger denominator, @JsonProperty("numerator") BigInteger numerator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Unable to compute gcd.");
        }
        val gcd = numerator.gcd(denominator);
        if (gcd.equals(BigInteger.ZERO)) {
            this.denominator = denominator;
            this.numerator = numerator;
        }else {
            this.denominator = denominator.divide(gcd);
            this.numerator = numerator.divide(gcd);
        }
    }

    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }
}
