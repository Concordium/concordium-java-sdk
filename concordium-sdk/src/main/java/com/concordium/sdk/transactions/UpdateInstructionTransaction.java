package com.concordium.sdk.transactions;

import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
public class UpdateInstructionTransaction extends BlockItem {

    private final UpdateInstructionTransactionHeader header;
    private final UpdateInstructionTransactionPayload payload;
    private final UpdateInstructionTransactionSignature signature;

    @Builder
    UpdateInstructionTransaction(
            final UpdateInstructionTransactionHeader header,
            final UpdateInstructionTransactionPayload payload,
            final UpdateInstructionTransactionSignature signature) {
        super(BlockItemType.UPDATE_INSTRUCTION);
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }

    @UnstableApi
    final byte[] getBlockItemBytes() {
        val signatureBytes = signature.getBytes();
        val headerBytes = header.getBytes();
        val payloadBytes = payload.getBytes();
        val buffer = ByteBuffer.allocate(signatureBytes.length + headerBytes.length + payloadBytes.length);
        buffer.put(signatureBytes);
        buffer.put(headerBytes);
        buffer.put(payloadBytes);

        return buffer.array();
    }
}
