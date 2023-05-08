package com.concordium.sdk.responses.blocksummary.updates;

import com.concordium.grpc.v2.AmountFraction;
import com.concordium.grpc.v2.Ratio;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

/**
 * A fraction
 */
@EqualsAndHashCode
@Getter
@Builder
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

    /**
     * Parses {@link Ratio} to {@link Fraction}
     * @param ratio {@link Ratio} returned by the GRPC V2 API
     * @return parsed {@link Fraction}
     */
    public static Fraction from(Ratio ratio) {
        return Fraction.builder()
                .numerator(BigInteger.valueOf(ratio.getNumerator()))
                .denominator(BigInteger.valueOf(ratio.getDenominator()))
                .build();
    }

    /**
     * Parses {@link AmountFraction} to {@link Fraction}.
     * {@link AmountFraction} represent the fraction parts_per_hundred_thousand/100_000
     * @param amountFraction {@link AmountFraction} returned by the GRPC V2 API
     * @return parsed {@link Fraction}
     */
    public static Fraction from(AmountFraction amountFraction) {
        if (amountFraction.getPartsPerHundredThousand() > 100_000) {throw new IllegalArgumentException("Parts per hundred thousand much not exceed 100_000");}
        return Fraction.builder()
                .numerator(BigInteger.valueOf(amountFraction.getPartsPerHundredThousand()))
                .denominator(BigInteger.valueOf(100_000))
                .build();
    }
}
