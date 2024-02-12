package com.concordium.sdk.requests.smartcontracts;

import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EuroAmount;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Represents energy, i.e. the cost of executing transactions on the chain.
 */
@EqualsAndHashCode
@Builder
@Getter
public class Energy {
    private final UInt64 value;

    @Override
    public String toString() {
        return this.value.toString() + " NRG";
    }

    public static Energy from(com.concordium.grpc.v2.Energy energy) {
        return new Energy(UInt64.from(energy.getValue()));
    }

    public static Energy from(UInt64 value) {
        return new Energy(value);
    }
    public static Energy from(String val) {
        return Energy.from(UInt64.from(val));
    }

    /**
     * Approximates the {@link EuroAmount} amount corresponding to the {@link Energy} using the provided {@link ChainParameters}.
     * Rounding may occur, so result might not be exact.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link EuroAmount} corresponding to the value of {@link Energy}.
     */
    public EuroAmount toEuro(ChainParameters parameters) {
        BigDecimal euroPerEnergy = parameters.getEuroPerEnergy().asBigDecimal(20);
        BigDecimal energy = new BigDecimal(this.getValue().toString());
        BigDecimal euros = energy.multiply(euroPerEnergy);
        return EuroAmount.from(euros.toString());
    }

    /**
     * Approximates the {@link CCDAmount} amount corresponding to the {@link Energy} using the provided {@link ChainParameters}.
     * Rounding may occur, so result might not be exact.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link CCDAmount} corresponding to the value of {@link Energy}.
     */
    public CCDAmount toCCD(ChainParameters parameters) {
        return this.toEuro(parameters).toCCD(parameters);
    }
}
