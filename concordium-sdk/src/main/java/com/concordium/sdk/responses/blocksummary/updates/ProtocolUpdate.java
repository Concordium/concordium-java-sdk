package com.concordium.sdk.responses.blocksummary.updates;

import com.concordium.sdk.transactions.Hash;
import lombok.*;

import java.util.Arrays;

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

    @Builder
    ProtocolUpdate(Hash specificationHash,
                   byte[] specificationAuxiliaryData,
                   String message,
                   String specificationURL) {
        this.specificationHash = specificationHash;
        this.specificationAuxiliaryData = Arrays.copyOf(specificationAuxiliaryData, specificationAuxiliaryData.length);
        this.message = message;
        this.specificationURL = specificationURL;
    }

    public static ProtocolUpdate from(com.concordium.grpc.v2.ProtocolUpdate protocolUpdate) {
        return ProtocolUpdate.builder()
                .message(protocolUpdate.getMessage())
                .specificationURL(protocolUpdate.getSpecificationUrl())
                .message(protocolUpdate.getMessage())
                .specificationAuxiliaryData(protocolUpdate.getSpecificationAuxiliaryData().toByteArray())
                .build();
    }

    public byte[] getSpecificationAuxiliaryData() {
        return Arrays.copyOf(specificationAuxiliaryData, specificationAuxiliaryData.length);
    }
}
