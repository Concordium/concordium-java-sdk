package com.concordium.sdk.responses;

import com.concordium.grpc.v2.Ratio;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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

    public Fraction(UInt64 numerator, UInt64 denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction from(Ratio value) {
        return Fraction
                .builder()
                .numerator(UInt64.from(value.getNumerator()))
                .denominator(UInt64.from(value.getDenominator()))
                .build();
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
