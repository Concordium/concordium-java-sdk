package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class UpdateEnqueuedPayloadUpdateResult {
    private final String specificationHash;
    private final String specificationAuxiliaryData;
    private final String message;
    private final String specificationURL;
    private final int denominator;
    private final int numerator;

    @JsonCreator
    UpdateEnqueuedPayloadUpdateResult(@JsonProperty("specificationHash") String specificationHash,
                                      @JsonProperty("specificationAuxiliaryData") String specificationAuxiliaryData,
                                      @JsonProperty("message") String message,
                                      @JsonProperty("specificationURL") String specificationURL,
                                      @JsonProperty("denominator") int denominator,
                                      @JsonProperty("numerator") int numerator) {
        this.specificationHash = specificationHash;
        this.specificationAuxiliaryData = specificationAuxiliaryData;
        this.message = message;
        this.specificationURL = specificationURL;
        this.denominator = denominator;
        this.numerator = numerator;
    }
}
