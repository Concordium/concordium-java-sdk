package com.concordium.sdk;

import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.BigFraction;
import com.concordium.sdk.types.UInt64;

import java.math.BigInteger;

/**
 * Utility class for converting between {@link CCDAmount}, {@link Energy} and Euros.
 * Represents converted values using {@link BigFraction} to ensure precision.
 */
public class Converter {

    /**
     * Converts {@link Energy} to euro using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the euro value of {@link Energy}.
     */
    public static BigFraction energyToEuro(Energy energy, ChainParameters parameters) {
        BigFraction energyFraction = BigFraction.from(energy.getValue(), UInt64.from(1));
        return energyToEuro(energyFraction, parameters);
    }

    /**
     * Converts {@link BigFraction} representing {@link Energy} to euro using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link BigFraction} representing an amount of {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the euro value of {@link Energy}.
     */
    public static BigFraction energyToEuro(BigFraction energy, ChainParameters parameters) {
        BigFraction euroPerEnergy = BigFraction.from(parameters.getEuroPerEnergy());
        return mult(energy, euroPerEnergy);
    }

    /**
     * Converts {@link Energy} to CCD using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the CCD value of {@link Energy}.
     */
    public static BigFraction energyToCCD(Energy energy, ChainParameters parameters) {
        BigFraction energyFraction = BigFraction.from(energy.getValue(), UInt64.from(1));
        return energyToCCD(energyFraction, parameters);
    }

    /**
     * Converts {@link BigFraction} representing {@link Energy} to CCD using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link BigFraction} representing an amount of {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the CCD value of {@link Energy}.
     */
    public static BigFraction energyToCCD(BigFraction energy, ChainParameters parameters) {
        BigFraction euros = energyToEuro(energy, parameters);
        return euroToCCD(euros, parameters);
    }

    /**
     * Converts {@link CCDAmount} to euros using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccdAmount {@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the euro value of {@link CCDAmount}.
     */
    public static BigFraction ccdToEuro(CCDAmount ccdAmount, ChainParameters parameters) {
        BigFraction ccd = BigFraction.from(ccdAmount.getValue(), UInt64.from(1));
        return ccdToEuro(ccd, parameters);
    }

    /**
     * Converts {@link BigFraction} representing {@link CCDAmount} to euros using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccd {@link BigFraction} representing {@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the euro value of {@link CCDAmount}.
     */
    public static BigFraction ccdToEuro(BigFraction ccd, ChainParameters parameters) {
        BigFraction microCCDPerEuro = BigFraction.from(parameters.getMicroCCDPerEuro());
        return div(ccd, microCCDPerEuro);
    }

    /**
     * Converts {@link CCDAmount} to energy using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccdAmount{@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the energy value of {@link CCDAmount}.
     */
    public static BigFraction ccdToEnergy(CCDAmount ccdAmount, ChainParameters parameters) {
        BigFraction ccd = BigFraction.from(ccdAmount.getValue(), UInt64.from(1));
        return ccdToEnergy(ccd, parameters);
    }

    /**
     * Converts {@link BigFraction} representing {@link CCDAmount} to energy using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccdAmount {@link BigFraction} representing {@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the energy value of {@link CCDAmount}.
     */
    public static BigFraction ccdToEnergy(BigFraction ccdAmount, ChainParameters parameters) {
        BigFraction euros = ccdToEuro(ccdAmount, parameters);
        return euroToEnergy(euros, parameters);
    }

    /**
     * Converts {@link BigFraction} representing an amount of euros to CCD using exchange rate from the provided {@link ChainParameters}.
     *
     * @param euros {@link BigFraction} representing amount of euros to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the CCD value of the input.
     */
    public static BigFraction euroToCCD(BigFraction euros, ChainParameters parameters) {
        BigFraction microCCDPerEuro = BigFraction.from(parameters.getMicroCCDPerEuro());
        return mult(euros, microCCDPerEuro);
    }

    /**
     * Converts {@link BigFraction} representing an amount of euros to energy using exchange rate from the provided {@link ChainParameters}.
     *
     * @param euros {@link BigFraction} representing amount of euros to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link BigFraction} corresponding to the energy value of the input.
     */
    public static BigFraction euroToEnergy(BigFraction euros, ChainParameters parameters) {
        BigFraction euroPerEnergy = BigFraction.from(parameters.getEuroPerEnergy());
        return div(euros,euroPerEnergy);
    }

    /**
     * Helper function for multiplying {@link BigFraction}s used during conversions.
     * Calculates a*c/b*d for input a/b, c/d.
    */
    private static BigFraction mult(BigFraction a, BigFraction b) {
        BigInteger numerator = a.getNumerator().multiply(b.getNumerator());
        BigInteger denominator = a.getDenominator().multiply(b.getDenominator());
        return BigFraction.from(numerator, denominator);
    }

    /**
     * Helper function for dividing {@link BigFraction}s used during conversions.
     * Calculates a*d/b*c for input a/b, c/d.
     */
    private static BigFraction div(BigFraction a, BigFraction b) {
        BigInteger numerator = a.getNumerator().multiply(b.getDenominator());
        BigInteger denominator = a.getDenominator().multiply(b.getNumerator());
        return BigFraction.from(numerator, denominator);
    }
}
