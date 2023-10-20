package com.concordium.sdk.responses.blockcertificates;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The signature of a {@link QuorumCertificate}.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class QuorumSignature {

    /**
     * The bytes representing the raw aggregate signature. The bytes have a fixed length of 48 bytes.
     */
    private final byte[] value;

    /**
     * Parses {@link com.concordium.grpc.v2.QuorumSignature} to {@link QuorumSignature}.
     * @param sig {@link com.concordium.grpc.v2.QuorumSignature} returned by the GRPC v2 API.
     * @return parsed {@link QuorumSignature}.
     */
    public static QuorumSignature from(com.concordium.grpc.v2.QuorumSignature sig) {
        return QuorumSignature.builder().value(sig.getValue().toByteArray()).build();
    }
}
