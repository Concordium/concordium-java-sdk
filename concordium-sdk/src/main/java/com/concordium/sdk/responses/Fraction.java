package com.concordium.sdk.responses;

import com.concordium.grpc.v2.Ratio;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.math.BigInteger;

/**
 * A fraction
 */
@EqualsAndHashCode
@Getter
@Builder
public class Fraction {

    /**
     * The numerator of the fraction.
     */
    private final BigInteger numerator;

    /**
     * The denominator of the fraction.
     */
    private final BigInteger denominator;

    @JsonCreator
    public Fraction(@JsonProperty("numerator") BigInteger numerator, @JsonProperty("denominator") BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Unable to compute gcd.");
        }
        val gcd = numerator.gcd(denominator);
        if (gcd.equals(BigInteger.ZERO)) {
            this.denominator = denominator;
            this.numerator = numerator;
        } else {
            this.denominator = denominator.divide(gcd);
            this.numerator = numerator.divide(gcd);
        }
    }

    public Fraction(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static Fraction from(Ratio value) {
        return Fraction.builder().numerator(BigInteger.valueOf(value.getNumerator())).denominator(BigInteger.valueOf(value.getDenominator())).build();
    }

    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }

    /**
     * Get the fraction as a floating point value.
     *
     * @return the floating point value.
     */
    public double asDouble() {
        return this.numerator.divide(this.denominator).doubleValue();
    }
}
