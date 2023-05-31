package com.concordium.sdk.responses.blocksummary.updates;

import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdatePayload;
import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdateType;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

/**
 * A protocol update
 */
@ToString
@Getter
@EqualsAndHashCode
@Builder
public class ProtocolUpdate implements UpdatePayload {

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

    /**
     * Parses {@link com.concordium.grpc.v2.ProtocolUpdate} to {@link ProtocolUpdate}.
     * @param protocolUpdate {@link com.concordium.grpc.v2.ProtocolUpdate} returned by the GRPC V2 API.
     * @return parsed {@link ProtocolUpdate}.
     */
    public static ProtocolUpdate parse(com.concordium.grpc.v2.ProtocolUpdate protocolUpdate) {
        return ProtocolUpdate.builder()
                .message(protocolUpdate.getMessage())
                .specificationURL(protocolUpdate.getSpecificationUrl())
                .specificationHash(Hash.from(protocolUpdate.getSpecificationHash().getValue().toByteArray()))
                .specificationAuxiliaryData(protocolUpdate.getSpecificationAuxiliaryData().toByteArray())
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.PROTOCOL_UPDATE;
    }
}
