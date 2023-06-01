package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.TimeParametersCpv1;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TimeParametersCPV1UpdatePayload implements UpdatePayload {

    /**
     * TODO delete this class
     * Length of a reward period in Epochs.
     */
    private UInt64 rewardPeriodLength;

    /**
     * Mint rate per payday.
     */
    private double mintPerPayday;

    /**
     * Parses {@link TimeParametersCpv1} to {@link TimeParametersCPV1UpdatePayload}.
     * @param timeParametersCpv1 {@link TimeParametersCpv1} returned by the GRPC V2 API.
     * @return parsed {@link TimeParametersCPV1UpdatePayload}.
     */
    public static TimeParametersCPV1UpdatePayload parse(TimeParametersCpv1 timeParametersCpv1) {
        return TimeParametersCPV1UpdatePayload.builder()
                .rewardPeriodLength(UInt64.from(timeParametersCpv1.getRewardPeriodLength().getValue().getValue()))
                .mintPerPayday(ParsingHelper.toMintRate(timeParametersCpv1.getMintPerPayday())).build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.TIME_PARAMETERS_CPV_1_UPDATE;
    }
}
