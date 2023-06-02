package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

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
}
