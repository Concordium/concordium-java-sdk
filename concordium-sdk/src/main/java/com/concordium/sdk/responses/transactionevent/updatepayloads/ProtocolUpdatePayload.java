package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.ProtocolUpdate;
import com.concordium.sdk.transactions.Hash;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ProtocolUpdatePayload implements UpdatePayload {

    /**
     * TODO delete this class as ProtocolUpdate is used instead
     * A brief message about the update.
     */
    private String message;

    /**
     * A URL of a document describing the update.
     */
    private String specificationUrl;

    /**
     * Hash of the specification document.
     */
    private Hash specificationHash;

    /**
     * Auxiliary data whose interpretation is defined by the new specification.
     */
    private byte[] specificationAuxiliaryData;

    /**
     * Parses {@link ProtocolUpdate} to {@link ProtocolUpdatePayload}.
     * @param protocolUpdate {@link ProtocolUpdate} returned by the GRPC V2 API.
     * @return parsed {@link ProtocolUpdatePayload}.
     */
    public static ProtocolUpdatePayload parse(ProtocolUpdate protocolUpdate) {
        return ProtocolUpdatePayload.builder()
                .message(protocolUpdate.getMessage())
                .specificationUrl(protocolUpdate.getSpecificationUrl())
                .specificationHash(Hash.from(protocolUpdate.getSpecificationHash().getValue().toByteArray()))
                .specificationAuxiliaryData(protocolUpdate.getSpecificationAuxiliaryData().toByteArray())
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.PROTOCOL_UPDATE;
    }
}
