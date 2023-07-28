package com.concordium.sdk.responses;

import com.concordium.grpc.v2.Ratio;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

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
    private final UInt64 numerator;

    /**
     * The denominator of the fraction.
     */
    private final UInt64 denominator;

    @JsonCreator
    public Fraction(@JsonProperty("numerator") UInt64 numerator, @JsonProperty("denominator") UInt64 denominator) {
        val num = numerator.getValue();
        val den = denominator.getValue();
        if (den == 0) {
            throw new IllegalArgumentException("Unable to compute gcd.");
        }

        val gcd = getGcd(num, den);
        if (gcd == 0) {
            this.numerator = UInt64.from(num);
            this.denominator = UInt64.from(den);
        } else {
            this.numerator = UInt64.from(num / gcd);
            this.denominator = UInt64.from(den / gcd);
        }
    }

    /**
     * get the greatest common divisor using the Euclidean algorithm.
     */
    private static long getGcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return getGcd(b, a % b);
    }

    public static Fraction from(Ratio value) {
        return Fraction.builder().numerator(UInt64.from(value.getNumerator())).denominator(UInt64.from(value.getDenominator())).build();
    }

    public static Fraction from(long num, long den) {
        return new Fraction(UInt64.from(num), UInt64.from(den));
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
        return (double) this.numerator.getValue() / (double) this.denominator.getValue();
    }
}
