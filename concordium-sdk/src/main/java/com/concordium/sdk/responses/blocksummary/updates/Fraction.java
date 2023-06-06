package com.concordium.sdk.responses.blocksummary.updates;

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
     * Parses {@link Ratio} to {@link Fraction}.
     * @param ratio {@link Ratio} returned by the GRPC V2 API.
     * @return parsed {@link Fraction}.
     */
    public static Fraction parse(Ratio ratio) {
        return Fraction.builder()
                .numerator(BigInteger.valueOf(ratio.getNumerator()))
                .denominator(BigInteger.valueOf(ratio.getDenominator()))
                .build();
    }

}
