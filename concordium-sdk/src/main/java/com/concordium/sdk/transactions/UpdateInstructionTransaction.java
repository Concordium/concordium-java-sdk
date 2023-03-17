package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UpdateInstructionTransaction extends BlockItem {

    private final UpdateInstructionTransactionHeader header;
    private final UpdateInstructionTransactionPayload payload;
    private final UpdateInstructionTransactionSignature signature;

    private UpdateInstructionTransaction(
            @NonNull final UpdateInstructionTransactionHeader header,
            @NonNull final UpdateInstructionTransactionPayload payload,
            @NonNull final UpdateInstructionTransactionSignature signature) {
        super(BlockItemType.UPDATE_INSTRUCTION);
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }

    /**
     * Creates a new instance of {@link UpdateInstructionTransaction}.
     * Using {@link UpdateInstructionTransactionHeader}, {@link UpdateInstructionTransactionSignature}
     * and Payload {@link UpdateInstructionTransactionPayload}.
     *
     * @param header    {@link UpdateInstructionTransactionHeader}.
     * @param signature {@link UpdateInstructionTransactionSignature}.
     * @param payload   {@link UpdateInstructionTransactionPayload} Payload for this transaction.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderMethodName = "builderBlockItem")
    static UpdateInstructionTransaction from(
            final UpdateInstructionTransactionHeader header,
            final UpdateInstructionTransactionPayload payload,
            final UpdateInstructionTransactionSignature signature) {
        try {
            return new UpdateInstructionTransaction(header, payload, signature);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
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
