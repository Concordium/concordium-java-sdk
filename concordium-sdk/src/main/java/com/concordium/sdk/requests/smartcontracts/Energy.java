package com.concordium.sdk.requests.smartcontracts;

import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EuroAmount;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
        return this.value.getValue() + " NRG";
    }

    public static Energy from(com.concordium.grpc.v2.Energy energy) {
        return new Energy(UInt64.from(energy.getValue()));
    }

    public static Energy from(UInt64 value) {
        return new Energy(value);
    }

    /**
     * Approximates the {@link EuroAmount} amount corresponding to the {@link Energy} using the provided {@link ChainParameters}.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link EuroAmount} corresponding to the value of {@link Energy}
     */
    public EuroAmount toEuro(ChainParameters parameters) {
        double euroPerEnergy = parameters.getEuroPerEnergy().asDouble();
        double euros = value.getValue() * euroPerEnergy;
        return EuroAmount.from(euros);
    }

    /**
     * Approximates the {@link CCDAmount} corresponding to the {@link Energy} using the provided {@link ChainParameters}.
     * @param parameters {@link ChainParameters} with exchange rate used for conversion.
     * @return {@link CCDAmount} corresponding to the value of {@link Energy}.
     */
    public CCDAmount toCCD(ChainParameters parameters) {
        return this.toEuro(parameters).toCCD(parameters);
    }
}
