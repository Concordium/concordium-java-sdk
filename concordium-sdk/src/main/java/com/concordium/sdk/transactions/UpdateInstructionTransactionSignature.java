package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.google.common.collect.ImmutableMap;
import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;
import lombok.*;

import java.nio.ByteBuffer;

import static com.concordium.sdk.transactions.TransactionSignature.SIGNATURE_SIZE;

@Builder
@EqualsAndHashCode
@ToString
public class UpdateInstructionTransactionSignature {
    @Singular
    private final ImmutableMap<Index, Signature> signatures;

    @UnstableApi
    byte[] getBytes() {
        val buffer = getByteBuffer();

        buffer.put((byte) signatures.size());
        for (Index index : signatures.keySet()) {
            buffer.put(index.getValue());
            buffer.put(UInt16.from(signatures.size()).getBytes());
            buffer.put(signatures.get(index).getBytes());
        }
        return buffer.array();
    }

    private ByteBuffer getByteBuffer() {
        int size = Index.BYTES;
        for (Signature ignored : signatures.values()) {
            size += Index.BYTES + UInt16.BYTES + SIGNATURE_SIZE;
        }

        return ByteBuffer.allocate(size);
    }
}
