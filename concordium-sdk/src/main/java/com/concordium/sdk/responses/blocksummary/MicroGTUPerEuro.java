package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class MicroGTUPerEuro {
    private final int denominator;
    private final int numerator;
    private final int threshold;
    private final List<Integer> authorizedKeys;
    private final int nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    MicroGTUPerEuro(@JsonProperty("denominator") int denominator,
                    @JsonProperty("numerator") int numerator,
                    @JsonProperty("threshold") int threshold,
                    @JsonProperty("authorizedKeys") List<Integer> authorizedKeys,
                    @JsonProperty("nextSequenceNumber") int nextSequenceNumber,
                    @JsonProperty("queue") List<Object> queue) {
        this.denominator = denominator;
        this.numerator = numerator;
        this.threshold = threshold;
        this.authorizedKeys = authorizedKeys;
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
