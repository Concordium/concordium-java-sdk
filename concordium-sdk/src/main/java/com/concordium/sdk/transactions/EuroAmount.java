package com.concordium.sdk.transactions;


import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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

    public Energy toEnergy(ChainParameters parameters) {
        double euroPerEnergy = parameters.getEuroPerEnergy().asDouble();
        Fraction flipped = Fraction.from(parameters.getEuroPerEnergy().getDenominator().getValue(), parameters.getEuroPerEnergy().getNumerator().getValue());
        return Energy.from(UInt64.from((long) (this.getValue() / euroPerEnergy)));

    }

    public CCDAmount toCCD(ChainParameters parameters) {
        double microCCDPerEuro = parameters.getMicroCCDPerEuro().asDouble();
        return CCDAmount.fromMicro((long) (this.getValue() * microCCDPerEuro));
    }

    @Override
    public String toString() {
        return this.getValue() + " Euros";
    }
}
