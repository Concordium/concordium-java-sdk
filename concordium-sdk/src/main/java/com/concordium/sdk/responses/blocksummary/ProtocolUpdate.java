package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class ProtocolUpdate {

    private final Hash specificationHash;
    private final String specificationAuxiliaryData;
    private final String message;
    private final String specificationURL;

    @JsonCreator
    ProtocolUpdate(@JsonProperty("specificationHash") Hash specificationHash,
                   @JsonProperty("specificationAuxiliaryData") String specificationAuxiliaryData,
                   @JsonProperty("message") String message,
                   @JsonProperty("specificationURL") String specificationURL) {
        this.specificationHash = specificationHash;
        this.specificationAuxiliaryData = specificationAuxiliaryData;
        this.message = message;
        this.specificationURL = specificationURL;
    }
}
