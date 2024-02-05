package com.concordium.sdk.transactions;

import com.concordium.grpc.v2.Amount;
import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * A CCD amount with 'micro' precision.
 */
@Getter
@EqualsAndHashCode
public class CCDAmount {
    public static final int BYTES = UInt64.BYTES;
    private final UInt64 value;

    private CCDAmount(UInt64 value) {
        this.value = value;
    }

    /**
     * Create an CCDAmount based on the input (treated as micro CCD)
     * @param val micro ccd
     * @return the amount
     */
    public static CCDAmount fromMicro(long val) {
        return new CCDAmount(UInt64.from(val));
    }

    /**
     * Create an CCDAmount based on the input
     * @param val micro ccd
     * @return the amount
     */
    public static CCDAmount from(long val) {
        return CCDAmount.fromMicro(val * 1000000);
    }

    public static CCDAmount fromMicro(String val) {
        return new CCDAmount(UInt64.from(val));
    }

    @JsonCreator
    CCDAmount(String amount) {
        this.value = UInt64.from(amount);
    }

    public static CCDAmount from(Amount cost) {
        return CCDAmount.fromMicro(cost.getValue());
    }

    byte[] getBytes() {
        return value.getBytes();
    }

    public static CCDAmount fromBytes(ByteBuffer source) {
        UInt64 value = UInt64.fromBytes(source);
        return new CCDAmount(value);
    }

    public EuroAmount toEuro(ChainParameters parameters) {
        double microCCDPerEuro = parameters.getMicroCCDPerEuro().asDouble();
        Fraction flipped = Fraction.from(parameters.getMicroCCDPerEuro().getDenominator().getValue(), parameters.getMicroCCDPerEuro().getNumerator().getValue());
        return EuroAmount.from(this.getValue().getValue() / microCCDPerEuro);
    }

    public Energy toEnergy(ChainParameters parameters) {
        return this.toEuro(parameters).toEnergy(parameters);
    }

    @Override
    public String toString() {
        return this.getValue().toString() + " micro CCD";
    }

    @JsonValue
    public String toJsonString() {
        return String.valueOf(this.value.getValue());
    }
}
