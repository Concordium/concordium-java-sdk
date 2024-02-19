package com.concordium.sdk;

import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.ConversionResult;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the different conversions between {@link Energy} and {@link CCDAmount}.
 */
public class EnergyToCCDTest {

    private static final long CCD_LONG = 1000000;
    private static final long ENERGY_LONG = 10000;

    private static final CCDAmount CCD_AMOUNT = CCDAmount.fromMicro(CCD_LONG);
    private static final Energy ENERGY = Energy.from(UInt64.from(ENERGY_LONG));

    private static final ConversionResult CCD_FRACTION = ConversionResult.from(CCD_AMOUNT.getValue(), UInt64.from(1));
    private static final ConversionResult ENERGY_FRACTION = ConversionResult.from(ENERGY.getValue(), UInt64.from(1));
    private static final ConversionResult EURO_FRACTION = ConversionResult.from(UInt64.from(10), UInt64.from(1));

    // Euro per energy doesn't change value and isn't nearly as big so can use same value for tests.
    private static final Fraction EURO_PER_ENERGY = new Fraction(UInt64.from("1"), UInt64.from("1100000"));

    // 16626124143116419072/78228883861
    private static final Fraction MICRO_CCD_PER_EURO_1 = new Fraction(UInt64.from("16626124143116419072"), UInt64.from("78228883861"));

    // 13578063771458994176/62220885881
    private static final Fraction MICRO_CCD_PER_EURO_2 = new Fraction(UInt64.from("13578063771458994176"), UInt64.from("62220885881"));

    // 3465558596452424320/16276715619
    private static final Fraction MICRO_CCD_PER_EURO_3 = new Fraction(UInt64.from("3465558596452424320"), UInt64.from("16276715619"));

    // 5414544070782746624/24832708375
    private static final Fraction MICRO_CCD_PER_EURO_4 = new Fraction(UInt64.from("5414544070782746624"), UInt64.from("24832708375"));
    private static final ChainParameters PARAM_1 = mock(ChainParameters.class);
    private static final ChainParameters PARAM_2 = mock(ChainParameters.class);
    private static final ChainParameters PARAM_3 = mock(ChainParameters.class);
    private static final ChainParameters PARAM_4 = mock(ChainParameters.class);

    private static final List<ChainParameters> PARAMETERS_LIST = new ArrayList<>();

    // Setup mock parameters with different ccd/euro ratios.
    static {
        when(PARAM_1.getMicroCCDPerEuro()).thenReturn(MICRO_CCD_PER_EURO_1);
        when(PARAM_2.getMicroCCDPerEuro()).thenReturn(MICRO_CCD_PER_EURO_2);
        when(PARAM_3.getMicroCCDPerEuro()).thenReturn(MICRO_CCD_PER_EURO_3);
        when(PARAM_4.getMicroCCDPerEuro()).thenReturn(MICRO_CCD_PER_EURO_4);

        when(PARAM_1.getEuroPerEnergy()).thenReturn(EURO_PER_ENERGY);
        when(PARAM_2.getEuroPerEnergy()).thenReturn(EURO_PER_ENERGY);
        when(PARAM_3.getEuroPerEnergy()).thenReturn(EURO_PER_ENERGY);
        when(PARAM_4.getEuroPerEnergy()).thenReturn(EURO_PER_ENERGY);

        PARAMETERS_LIST.add(PARAM_1);
        PARAMETERS_LIST.add(PARAM_2);
        PARAMETERS_LIST.add(PARAM_3);
        PARAMETERS_LIST.add(PARAM_4);
    }


    @SneakyThrows
    @Test
    public void testShouldBePrecise() {
            for (ChainParameters parameters : PARAMETERS_LIST) {
                // Converting Energy
                energyToCCD(parameters);
                energyToEuro(parameters);
                // Converting CCD
                ccdToEnergy(parameters);
                ccdToEuro(parameters);
                // Converting Euro
                euroToEnergy(parameters);
                euroToCCD(parameters);
            }
    }

    private void energyToCCD(ChainParameters parameters) {
        ConversionResult ccdFromEnergy = CurrencyConverter.energyToMicroCCD(ENERGY, parameters);
        ConversionResult ccdFromEnergyFraction = CurrencyConverter.energyToMicroCCD(ENERGY_FRACTION, parameters);
        // Both conversions yield same result
        assertEquals(ccdFromEnergy, ccdFromEnergyFraction);

        ConversionResult energyFromCCD = CurrencyConverter.ccdToEnergy(ccdFromEnergy, parameters);
        ConversionResult energyFromCCDFraction = CurrencyConverter.ccdToEnergy(ccdFromEnergyFraction, parameters);

        // Converting back yields same result
        assertEquals(energyFromCCD, energyFromCCDFraction);

        // Converting back is precise
        assertEquals(energyFromCCD, ENERGY_FRACTION);
    }

    private void energyToEuro(ChainParameters parameters) {
        ConversionResult euroFromEnergy = CurrencyConverter.energyToEuro(ENERGY, parameters);
        ConversionResult euroFromEnergyFraction = CurrencyConverter.energyToEuro(ENERGY_FRACTION, parameters);
        // Both conversions yield same result
        assertEquals(euroFromEnergy, euroFromEnergyFraction);

        ConversionResult energyFromEuro = CurrencyConverter.euroToEnergy(euroFromEnergy, parameters);
        ConversionResult energyFromEuroFraction = CurrencyConverter.euroToEnergy(euroFromEnergyFraction, parameters);

        // Converting back yields same result
        assertEquals(energyFromEuro, energyFromEuroFraction);

        // Converting back is precise
        assertEquals(energyFromEuro, ENERGY_FRACTION);
    }
    private void ccdToEnergy(ChainParameters parameters) {
        ConversionResult energyFromCCD = CurrencyConverter.ccdToEnergy(CCD_AMOUNT, parameters);
        ConversionResult energyFromCCDFraction = CurrencyConverter.ccdToEnergy(CCD_FRACTION, parameters);

        // Both conversions yield same result
        assertEquals(energyFromCCD, energyFromCCDFraction);

        ConversionResult ccdFromEnergy = CurrencyConverter.energyToMicroCCD(energyFromCCD, parameters);
        ConversionResult ccdFromEnergyFraction = CurrencyConverter.energyToMicroCCD(energyFromCCDFraction, parameters);

        // Converting back yields same result
        assertEquals(ccdFromEnergy, ccdFromEnergyFraction);

        // Converting back is precise
        assertEquals(ccdFromEnergy, CCD_FRACTION);
    }
    private void ccdToEuro(ChainParameters parameters) {
        ConversionResult euroFromCCD = CurrencyConverter.microCCDToEuro(CCD_AMOUNT, parameters);
        ConversionResult euroFromCCDFraction = CurrencyConverter.microCCDToEuro(CCD_FRACTION, parameters);
        // Both conversions yield same result
        assertEquals(euroFromCCD,euroFromCCDFraction);

        ConversionResult ccdFromEuro = CurrencyConverter.euroToMicroCCD(euroFromCCD, parameters);
        ConversionResult ccdFromEuroFraction = CurrencyConverter.euroToMicroCCD(euroFromCCDFraction, parameters);

        // Converting back yields same result
        assertEquals(ccdFromEuro, ccdFromEuroFraction);

        // Converting back is precise
        assertEquals(ccdFromEuro, CCD_FRACTION);
    }
    private void euroToEnergy(ChainParameters parameters) {
        ConversionResult energyFromEuro = CurrencyConverter.euroToEnergy(EURO_FRACTION, parameters);
        ConversionResult euroFromEnergy = CurrencyConverter.energyToEuro(energyFromEuro, parameters);

        // Conversions are precise
        assertEquals(euroFromEnergy, EURO_FRACTION);
    }
    private void euroToCCD(ChainParameters parameters) {
        ConversionResult ccdFromEuro = CurrencyConverter.euroToMicroCCD(EURO_FRACTION, parameters);
        ConversionResult euroFromCCD = CurrencyConverter.microCCDToEuro(ccdFromEuro, parameters);

        // Conversions are precise
        assertEquals(euroFromCCD, EURO_FRACTION);
    }

    /**
     * Asserts that the calculations performed when using PARAM_1 yield the correct result.
     */
    @Test
    public void testActualValues() {
        // EURO_PER_ENERGY * ENERGY = 1/1100000×10000 = 1/110
        ConversionResult expectedEuroFromEnergy = ConversionResult.from("1", "110");
        ConversionResult euroFromEnergy = CurrencyConverter.energyToEuro(ENERGY, PARAM_1);
        assertEquals(expectedEuroFromEnergy, euroFromEnergy);

        // MICRO_CCD_PER_EURO_1 * EURO = 16626124143116419072/78228883861 * 10 = 166261241431164190720/78228883861
        ConversionResult expectedCCDFromEuro = ConversionResult.from("166261241431164190720","78228883861");
        ConversionResult ccdFromEuro = CurrencyConverter.euroToMicroCCD(EURO_FRACTION, PARAM_1);
        assertEquals(expectedCCDFromEuro, ccdFromEuro);

        // EURO_PER_ENERGY * ENERGY * MICRO_CCD_PER_EURO_1 = 1/110 * 16626124143116419072/78228883861 = 8313062071558209536/4302588612355
        ConversionResult expectedCCDFromEnergy = ConversionResult.from("8313062071558209536", "4302588612355");
        ConversionResult ccdFromEnergy = CurrencyConverter.energyToMicroCCD(ENERGY, PARAM_1);
        assertEquals(expectedCCDFromEnergy, ccdFromEnergy);

        // CCD_AMOUNT / MICRO_CCD_PER_EURO_1 = 1000000 / (16626124143116419072/78228883861) = 1222326310328125/259783189736194048
        ConversionResult expectedEuroFromCCD = ConversionResult.from("1222326310328125", "259783189736194048");
        ConversionResult euroFromCCD = CurrencyConverter.microCCDToEuro(CCD_AMOUNT, PARAM_1);
        assertEquals(expectedEuroFromCCD, euroFromCCD);

        // EUROS / EURO_PER_ENERGY = 10 / (1/1100000) = 11000000
        ConversionResult expectedEnergyFromEuro = ConversionResult.from("11000000", "1");
        ConversionResult energyFromEuro = CurrencyConverter.euroToEnergy(EURO_FRACTION, PARAM_1);
        assertEquals(expectedEnergyFromEuro, energyFromEuro);

        // CCD_AMOUNT / MICRO_CCD_PER_EURO_1 / EUROS_PER_ENERGY =
        // (1000000 / (16626124143116419072/78228883861)) / (1/1100000) =
        // 42017466917529296875/8118224679256064
        ConversionResult expectedEnergyFromCCD = ConversionResult.from("42017466917529296875", "8118224679256064");
        ConversionResult energyFromCCD = CurrencyConverter.ccdToEnergy(CCD_AMOUNT, PARAM_1);
        assertEquals(expectedEnergyFromCCD, energyFromCCD);
    }

}
