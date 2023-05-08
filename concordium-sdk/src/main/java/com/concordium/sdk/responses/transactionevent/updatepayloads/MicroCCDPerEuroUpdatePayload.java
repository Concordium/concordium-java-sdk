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
public class MicroCCDPerEuroUpdatePayload implements UpdatePayload {

    private Fraction value;

    /**
     * Parses {@link ExchangeRate} to {@link MicroCCDPerEuroUpdatePayload}
     * @param exchangeRate {@link ExchangeRate} returned by the GRPC V2 API
     * @return parsed {@link ExchangeRate}
     */
    public static MicroCCDPerEuroUpdatePayload parse(ExchangeRate exchangeRate) {
        return MicroCCDPerEuroUpdatePayload.builder()
                .value(Fraction.from(exchangeRate.getValue()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.MICRO_CCD_PER_EURO_UPDATE;
    }
}
