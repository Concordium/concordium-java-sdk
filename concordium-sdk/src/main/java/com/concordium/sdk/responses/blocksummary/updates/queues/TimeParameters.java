package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
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

    @JsonCreator
    TimeParameters(@JsonProperty("rewardPeriodLength") long rewardPeriodLength, @JsonProperty("mintPerDay") double mintPerPayday) {
        this.rewardPeriodLength = rewardPeriodLength;
        this.mintPerPayday = mintPerPayday;
    }
}
