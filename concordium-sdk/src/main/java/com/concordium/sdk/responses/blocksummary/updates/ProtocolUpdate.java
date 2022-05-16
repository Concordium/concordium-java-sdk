package com.concordium.sdk.responses.blocksummary.updates;

import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

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
    private final byte[] specificationAuxiliaryData;

    /**
     * A brief message about the update
     */
    private final String message;

    /**
     * A URL of a document describing the update
     */
    private final String specificationURL;

    @SneakyThrows
    @JsonCreator
    ProtocolUpdate(@JsonProperty("specificationHash") Hash specificationHash,
                   @JsonProperty("specificationAuxiliaryData") String specificationAuxiliaryData,
                   @JsonProperty("message") String message,
                   @JsonProperty("specificationURL") String specificationURL) {
        this.specificationHash = specificationHash;
        this.specificationAuxiliaryData = Hex.decodeHex(specificationAuxiliaryData);
        this.message = message;
        this.specificationURL = specificationURL;
    }
}
