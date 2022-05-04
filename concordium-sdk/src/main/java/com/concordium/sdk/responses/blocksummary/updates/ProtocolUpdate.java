package com.concordium.sdk.responses.blocksummary.updates;

import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A protocol update
 */
@ToString
@Getter
@EqualsAndHashCode
public class ProtocolUpdate {

    /**
     * SHA256 hash of the specification document
     */
    private final Hash specificationHash;

    /**
     * Auxiliary data whose interpretation is defined by the new specification
     */
    private final String specificationAuxiliaryData;

    /**
     * A brief message about the update
     */
    private final String message;

    /**
     * A URL of a document describing the update
     */
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
