package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.transactions.Signature;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A (non-aggregate) signature of a validator. This is used to sign blocks produced by
 * the validator as well as some finalization messages.
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class BlockSignature {

    private final Signature value;

    public static BlockSignature from(com.concordium.grpc.v2.BlockSignature signature) {
        return BlockSignature.builder()
                .value(Signature.from(signature.getValue().toByteArray()))
                .build();
    }
}
