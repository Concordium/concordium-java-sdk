package com.concordium.sdk.responses.chainparameters;

import com.concordium.grpc.v2.TimeParametersCpv1;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Time parameters exists from protocol version 4 and onwards.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class TimeParameters {

    /**
     * Length of a reward period (a number of epochs).
     */
    private final long rewardPeriodLength;

    /**
     * Mint rate per payday (as a proportion of the extant supply).
     * Note. The Concordium node returns a floating point number with arbitrary precision so the
     * 'mintPerPayday' might be rounded (since a 'double' has a precision of 15 decimals).
     */
    private final double mintPerPayday;

    public static TimeParameters from(TimeParametersCpv1 update) {
        return TimeParameters
                .builder()
                .rewardPeriodLength(update.getRewardPeriodLength().getValue().getValue())
                //mantissa * 10^(-exponent)
                .mintPerPayday(update.getMintPerPayday().getMantissa() * Math.pow(10, -1 * update.getMintPerPayday().getExponent()))
                .build();
    }
}
