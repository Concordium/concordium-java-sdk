package com.concordium.sdk.transactions;


import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An amount of Euros.
 */
@Getter
@EqualsAndHashCode
public class EuroAmount {

    private final double value;

    private EuroAmount(double value) {
        this.value = value;
    }

    public static EuroAmount from(double value) {
        return new EuroAmount(value);
    }
    public static EuroAmount from(String value) {return EuroAmount.from(Double.parseDouble(value));}

    public Energy toEnergy(ChainParameters parameters) {
        BigDecimal euroPerEnergy = parameters.getEuroPerEnergy().asBigDecimal(20);
        BigDecimal euroAmount = BigDecimal.valueOf(this.getValue());
        BigDecimal energy = euroAmount.divide(euroPerEnergy, 20, RoundingMode.HALF_UP);
        return Energy.from(UInt64.from(energy.longValue()));

    }

    public CCDAmount toCCD(ChainParameters parameters) {
        BigDecimal microCCDPerEuro = parameters.getMicroCCDPerEuro().asBigDecimal(20);
        BigDecimal euroAmount = BigDecimal.valueOf(this.getValue());
        BigDecimal ccd = euroAmount.multiply(microCCDPerEuro);
        return CCDAmount.fromMicro(ccd.longValue());
    }

    @Override
    public String toString() {
        return this.getValue() + " Euros";
    }
}
