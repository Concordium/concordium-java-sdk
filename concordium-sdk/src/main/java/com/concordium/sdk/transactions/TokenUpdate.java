package com.concordium.sdk.transactions;


import com.concordium.grpc.v2.plt.TokenId;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.List;

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
    @Singular
    private final List<TokenOperation> operations;

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TOKEN_UPDATE;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        val symbolBytes = tokenSymbol.getValueBytes();

        val buffer = ByteBuffer.allocate(
                Byte.BYTES + symbolBytes.size()
                        + UInt32.BYTES + operations.size()
        );

        buffer.put((byte) symbolBytes.size());
        buffer.put(symbolBytes.asReadOnlyByteBuffer());
        buffer.putInt(operations.size());
        // TODO Encode operations array.

        return buffer.array();
    }

    public UInt64 getOperationsBaseCost() {
        var total = 0L;
        for (TokenOperation operation : operations) {
            total += operation.getBaseCost().getValue();
        }
        return UInt64.from(total);
    }
}
