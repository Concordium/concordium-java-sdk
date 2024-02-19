package com.concordium.sdk.types;

import com.concordium.sdk.CurrencyConverter;
import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * For representing fractions with large numerators or denominators. Used in {@link CurrencyConverter} to convert to/from {@link CCDAmount} and {@link Energy}.
 * Use {@link ConversionResult#asBigDecimal(int)} to get numeric value of the fraction.
 */
@Getter
@Builder

public class ConversionResult{

    /**
     * Numerator of the fraction.
     */
    private BigInteger numerator;
    /**
     * Denominator of the fraction.
     */
    private BigInteger denominator;

    public static ConversionResult from(BigInteger numerator, BigInteger denominator) {
        return new ConversionResult(numerator, denominator);
    }

    public static ConversionResult from(UInt64 numerator, UInt64 denominator) {
        return from(numerator.toString(),denominator.toString());
    }

    public static ConversionResult from(Fraction fraction) {
        return from(fraction.getNumerator(),fraction.getDenominator());
    }

    public static ConversionResult from(String numerator, String denominator) {
        return from(new BigInteger(numerator), new BigInteger(denominator));
    }

    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }

    /**
     * Get the fraction as a {@link BigDecimal} value with the specified amount of precision.
     * Result is rounded using {@link RoundingMode#HALF_UP}
     *
     * @param precision how many decimals of precision.
     * @return the fraction represented as a {@link BigDecimal}.
     */
    public BigDecimal asBigDecimal(int precision) {
        return asBigDecimal(precision, RoundingMode.HALF_UP);
    }

    /**
     * Get the fraction as a {@link BigDecimal} value with the specified amount of precision.
     * Result is rounded using the provided {@link RoundingMode}.
     *
     * @param precision how many decimals of precision.
     * @param roundingMode the {@link RoundingMode} to use.
     * @return the fraction represented as a {@link BigDecimal}.
     */
    public BigDecimal asBigDecimal(int precision, RoundingMode roundingMode) {
        BigDecimal numerator = new BigDecimal(this.getNumerator());
        BigDecimal denominator = new BigDecimal(this.getDenominator());
        return numerator.divide(denominator, precision, roundingMode);
    }

    /**
     * Multiplies {@link ConversionResult}s.
     * Calculates a*c/b*d for input a/b, c/d.
     */
    public ConversionResult mult(ConversionResult other) {
        BigInteger numerator = this.getNumerator().multiply(other.getNumerator());
        BigInteger denominator = this.getDenominator().multiply(other.getDenominator());
        return ConversionResult.from(numerator, denominator);
    }

    /**
     * Helper function for dividing {@link ConversionResult}s used during conversions.
     * Calculates a*d/b*c for input a/b, c/d.
     */
    public ConversionResult div(ConversionResult other) {
        BigInteger numerator = this.getNumerator().multiply(other.getDenominator());
        BigInteger denominator = this.getDenominator().multiply(other.getNumerator());
        return ConversionResult.from(numerator, denominator);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof ConversionResult)) {
            return false;
        }

        ConversionResult otherFraction = (ConversionResult) o;

        // Comparison done using cross multiplication. a * d = b * c => a/b = c/d
        BigInteger ad = this.numerator.multiply(otherFraction.denominator);
        BigInteger bc = this.denominator.multiply(otherFraction.numerator);
        return ad.equals(bc);
    }

}
