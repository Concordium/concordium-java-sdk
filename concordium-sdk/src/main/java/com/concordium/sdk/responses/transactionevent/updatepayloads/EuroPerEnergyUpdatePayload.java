package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.ExchangeRate;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class EuroPerEnergyUpdatePayload implements UpdatePayload {

    private Fraction value;

    /**
     * Parses {@link ExchangeRate} to {@link EuroPerEnergyUpdatePayload}.
     * @param exchangeRate {@link ExchangeRate} returned by the GRPC V2 API.
     * @return parsed {@link EuroPerEnergyUpdatePayload}.
     */
    public static EuroPerEnergyUpdatePayload parse(ExchangeRate exchangeRate){
        return EuroPerEnergyUpdatePayload.builder()
                .value(Fraction.from(exchangeRate.getValue()))
                .build();

    }
    @Override
    public UpdateType getType() {
        return UpdateType.EURO_PER_ENERGY_UPDATE;
    }
}
