package com.concordium.sdk.transactions;


import com.concordium.grpc.v2.plt.TokenId;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

/**
 * A protocol-level token (PLT) transaction payload
 * containing the actual operations.
 */
@ToString
@Builder
@Getter
@EqualsAndHashCode(callSuper = true)
public class TokenUpdate extends Payload {

    /**
     * Symbol (ID) of the token to execute operations on.
     */
    private final TokenId tokenSymbol;

    /**
     * Operations to execute.
     */
    private final byte[] operations;

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TOKEN_UPDATE;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        val symbolBytes = tokenSymbol.getValueBytes();
        // TODO Encode operations with CBOR.
        val operationsBytes = operations;

        val buffer = ByteBuffer.allocate(
                Byte.BYTES + symbolBytes.size()
                        + UInt32.BYTES + operations.length
        );

        buffer.put((byte) symbolBytes.size());
        buffer.put(symbolBytes.asReadOnlyByteBuffer());
        buffer.putInt(operations.length);
        buffer.put(operations);

        return buffer.array();
    }

    public UInt64 getOperationsBaseCost() {
        // TODO Sum base costs of operations.
        return UInt64.from(100);
    }
}
