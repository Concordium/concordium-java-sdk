package com.concordium.sdk.responses.blockcertificates;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A proof that establishes that the successor block of
 * a {@link EpochFinalizationEntry} is the immediate successor of
 * the finalized block.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class SuccessorProof {
    /**
     * The proof represented as raw bytes. The bytes have a fixed length of 32 bytes.
     */
    private final byte[] value;

    /**
     * Parses {@link com.concordium.grpc.v2.SuccessorProof} to {@link SuccessorProof}.
     * @param proof {@link com.concordium.grpc.v2.SuccessorProof} returned by the GRCP v2 API.
     * @return parsed {@link SuccessorProof}.
     */
    public static SuccessorProof from(com.concordium.grpc.v2.SuccessorProof proof) {
        return SuccessorProof.builder().value(proof.getValue().toByteArray()).build();
    }
}
