package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An inclusive range consisting of a minimum value and a maximum value.
 */
@ToString
@EqualsAndHashCode
@Getter
public class Range {
    private final double min;
    private final double max;

    @JsonCreator
    Range(@JsonProperty("min") double min, @JsonProperty("max") double max) {
        this.min = min;
        this.max = max;
    }
}
