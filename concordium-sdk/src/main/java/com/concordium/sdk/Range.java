package com.concordium.sdk;

import com.concordium.grpc.v2.InclusiveRangeAmountFraction;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * An inclusive range consisting of a minimum value and a maximum value.
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
@Jacksonized
public class Range {
    @JsonProperty("min")
    private final double min;
    @JsonProperty("max")
    private final double max;

    public static Range from(InclusiveRangeAmountFraction baking) {
        return Range
                .builder()
                .min(PartsPerHundredThousand.from(baking.getMin().getPartsPerHundredThousand()).asDouble())
                .max(PartsPerHundredThousand.from(baking.getMax().getPartsPerHundredThousand()).asDouble())
                .build();
    }
}
