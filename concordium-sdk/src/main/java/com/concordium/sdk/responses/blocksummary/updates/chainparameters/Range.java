package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.concordium.grpc.v2.InclusiveRangeAmountFraction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * An inclusive range consisting of a minimum value and a maximum value.
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
public class Range {
    private final double min;
    private final double max;

    @JsonCreator
    Range(@JsonProperty("min") double min, @JsonProperty("max") double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Parses {@link InclusiveRangeAmountFraction} to {@link Range}.
     * @param inclusiveRangeAmountFraction {@link InclusiveRangeAmountFraction} returned by the GRPC V2 API.
     * @return parsed {@link Range}
     */
    public static Range from(InclusiveRangeAmountFraction inclusiveRangeAmountFraction) {
        val min = inclusiveRangeAmountFraction.getMin().getPartsPerHundredThousand();
        val max = inclusiveRangeAmountFraction.getMax().getPartsPerHundredThousand();
        if (min > 100_000 || max > 100_000) {throw new IllegalArgumentException("Parts per hundred thousand much not exceed 100_000");}
        return Range.builder()
                .min((double) min / 100_000)
                .max((double) max / 100_000)
                .build();
    }
}
