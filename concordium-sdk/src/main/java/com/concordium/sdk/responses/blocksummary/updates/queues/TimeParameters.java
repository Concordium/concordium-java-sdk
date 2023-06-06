package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.grpc.v2.TimeParametersCpv1;
import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdatePayload;
import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdateType;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class TimeParameters implements UpdatePayload {

    /**
     * Length of a reward period (a number of epochs).
     */
    private final UInt64 rewardPeriodLength;

    /**
     * Mint rate per payday (as a proportion of the extant supply).
     * Note. The Concordium node returns a floating point number with arbitrary precision so the
     * 'mintPerPayday' might be rounded (since a 'double' has a precision of 15 decimals).
     */
    private final double mintPerPayday;

    @JsonCreator
    TimeParameters(@JsonProperty("rewardPeriodLength") UInt64 rewardPeriodLength, @JsonProperty("mintPerDay") double mintPerPayday) {
        this.rewardPeriodLength = rewardPeriodLength;
        this.mintPerPayday = mintPerPayday;
    }

    /**
     * Parses {@link TimeParametersCpv1} to {@link TimeParameters}.
     * @param timeParametersCpv1 {@link TimeParametersCpv1} returned by the GRPC V2 API.
     * @return parsed {@link TimeParameters}.
     */
    public static TimeParameters parse(TimeParametersCpv1 timeParametersCpv1) {
        return TimeParameters.builder()
                .rewardPeriodLength(UInt64.from(timeParametersCpv1.getRewardPeriodLength().getValue().getValue()))
                .mintPerPayday(timeParametersCpv1.getMintPerPayday().getMantissa()*Math.pow(10, -1 * timeParametersCpv1.getMintPerPayday().getExponent()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.TIME_PARAMETERS_CPV_1_UPDATE;
    }
}
