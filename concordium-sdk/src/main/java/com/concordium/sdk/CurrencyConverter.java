package com.concordium.sdk;

import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.ConversionResult;
import com.concordium.sdk.types.UInt64;

import static com.concordium.sdk.types.ConversionResult.*;

/**
 * Utility class for converting between {@link CCDAmount}, {@link Energy} and Euros.
 * Represents converted values using {@link ConversionResult} to ensure precision.
 */
public class CurrencyConverter {

    /**
     * Converts {@link Energy} to euro using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the euro value of {@link Energy}.
     */
    public static ConversionResult energyToEuro(Energy energy, ChainParameters parameters) {
        ConversionResult energyFraction = from(energy.getValue(), UInt64.from(1));
        return energyToEuro(energyFraction, parameters);
    }

    /**
     * Converts {@link ConversionResult} representing {@link Energy} to euro using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link ConversionResult} representing an amount of {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the euro value of {@link Energy}.
     */
    public static ConversionResult energyToEuro(ConversionResult energy, ChainParameters parameters) {
        ConversionResult euroPerEnergy = from(parameters.getEuroPerEnergy());
        return energy.mult(euroPerEnergy);
    }

    /**
     * Converts {@link Energy} to micro CCD using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the micro CCD value of {@link Energy}.
     */
    public static ConversionResult energyToMicroCCD(Energy energy, ChainParameters parameters) {
        ConversionResult energyFraction = from(energy.getValue(), UInt64.from(1));
        return energyToMicroCCD(energyFraction, parameters);
    }

    /**
     * Converts {@link ConversionResult} representing {@link Energy} to micro CCD using exchange rate from the provided {@link ChainParameters}.
     *
     * @param energy {@link ConversionResult} representing an amount of {@link Energy} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the micro CCD value of {@link Energy}.
     */
    public static ConversionResult energyToMicroCCD(ConversionResult energy, ChainParameters parameters) {
        ConversionResult euros = energyToEuro(energy, parameters);
        return euroToMicroCCD(euros, parameters);
    }

    /**
     * Converts {@link CCDAmount} to euros using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccdAmount {@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the euro value of {@link CCDAmount}.
     */
    public static ConversionResult microCCDToEuro(CCDAmount ccdAmount, ChainParameters parameters) {
        ConversionResult ccd = from(ccdAmount.getValue(), UInt64.from(1));
        return microCCDToEuro(ccd, parameters);
    }

    /**
     * Converts {@link ConversionResult} representing {@link CCDAmount} to euros using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccd {@link ConversionResult} representing {@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the euro value of {@link CCDAmount}.
     */
    public static ConversionResult microCCDToEuro(ConversionResult ccd, ChainParameters parameters) {
        ConversionResult microCCDPerEuro = from(parameters.getMicroCCDPerEuro());
        return ccd.div(microCCDPerEuro);
    }

    /**
     * Converts {@link CCDAmount} to energy using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccdAmount{@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the energy value of {@link CCDAmount}.
     */
    public static ConversionResult ccdToEnergy(CCDAmount ccdAmount, ChainParameters parameters) {
        ConversionResult ccd = from(ccdAmount.getValue(), UInt64.from(1));
        return ccdToEnergy(ccd, parameters);
    }

    /**
     * Converts {@link ConversionResult} representing {@link CCDAmount} to energy using exchange rate from the provided {@link ChainParameters}.
     *
     * @param ccdAmount {@link ConversionResult} representing {@link CCDAmount} to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the energy value of {@link CCDAmount}.
     */
    public static ConversionResult ccdToEnergy(ConversionResult ccdAmount, ChainParameters parameters) {
        ConversionResult euros = microCCDToEuro(ccdAmount, parameters);
        return euroToEnergy(euros, parameters);
    }

    /**
     * Converts {@link ConversionResult} representing an amount of euros to micro CCD using exchange rate from the provided {@link ChainParameters}.
     *
     * @param euros {@link ConversionResult} representing amount of euros to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the micro CCD value of the input.
     */
    public static ConversionResult euroToMicroCCD(ConversionResult euros, ChainParameters parameters) {
        ConversionResult microCCDPerEuro = from(parameters.getMicroCCDPerEuro());
        return euros.mult(microCCDPerEuro);
    }

    /**
     * Converts {@link ConversionResult} representing an amount of euros to energy using exchange rate from the provided {@link ChainParameters}.
     *
     * @param euros {@link ConversionResult} representing amount of euros to convert.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link ConversionResult} corresponding to the energy value of the input.
     */
    public static ConversionResult euroToEnergy(ConversionResult euros, ChainParameters parameters) {
        ConversionResult euroPerEnergy = from(parameters.getEuroPerEnergy());
        return euros.div(euroPerEnergy);
    }

}
