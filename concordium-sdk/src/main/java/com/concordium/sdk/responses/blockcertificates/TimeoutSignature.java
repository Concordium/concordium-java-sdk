package com.concordium.sdk.responses.blockcertificates;

import com.concordium.sdk.crypto.BLSSignature;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The signature of a {@link TimeoutCertificate}.
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TimeoutSignature {
    /**
     * The aggregate {@link BLSSignature}.
     */
    private final BLSSignature value;

    /**
     * Parses {@link com.concordium.grpc.v2.TimeoutSignature} to {@link TimeoutSignature}.
     * @param sig {@link com.concordium.grpc.v2.TimeoutSignature} returned by the GRCP v2 API.
     * @return parsed {@link TimeoutSignature}.
     */
    public static TimeoutSignature from(com.concordium.grpc.v2.TimeoutSignature sig) {
        return TimeoutSignature.builder()
                .value(BLSSignature.from(sig.getValue().toByteArray()))
                .build();
    }
}
