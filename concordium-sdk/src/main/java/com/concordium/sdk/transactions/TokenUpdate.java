package com.concordium.sdk.transactions;


import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.TokenOperation;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
    private final String tokenSymbol;

    /**
     * Operations to execute.
     */
    @Singular
    private final List<TokenOperation> operations;

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TOKEN_UPDATE;
    }

    /**
     * @return CBOR-serialized operations.
     */
    @SneakyThrows
    public byte[] getOperationsSerialized() {
        return CborMapper.INSTANCE
                .writerFor(TOKEN_OPERATION_LIST_TYPE)
                .writeValueAsBytes(operations);
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        val symbolBytes = tokenSymbol.getBytes(StandardCharsets.UTF_8);
        val operationsBytes = getOperationsSerialized();

        val buffer = ByteBuffer.allocate(
                Byte.BYTES + symbolBytes.length
                        + UInt32.BYTES + operationsBytes.length
        );
        buffer.put((byte) symbolBytes.length);
        buffer.put(symbolBytes);
        buffer.putInt(operationsBytes.length);
        buffer.put(operationsBytes);

        return buffer.array();
    }

    public UInt64 getOperationsBaseCost() {
        var total = UInt64.from(0L);
        for (TokenOperation operation : operations) {
            total = total.plus(operation.getBaseCost());
        }
        return total;
    }

    private static final TypeReference<List<TokenOperation>> TOKEN_OPERATION_LIST_TYPE =
            new TypeReference<List<TokenOperation>>() {
                // TypeReference is needed as Java erases types from generics
                // leaving the serializer clueless about the TokenOperation hierarchy.
            };
}
