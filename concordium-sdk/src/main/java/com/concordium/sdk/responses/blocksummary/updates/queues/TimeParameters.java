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
     */
    private final MintRate mintPerPayday;

    @JsonCreator
    TimeParameters(@JsonProperty("rewardPeriodLength") long rewardPeriodLength, @JsonProperty("mintPerDay") MintRate mintPerPayday) {
        this.rewardPeriodLength = rewardPeriodLength;
        this.mintPerPayday = mintPerPayday;
    }
}
