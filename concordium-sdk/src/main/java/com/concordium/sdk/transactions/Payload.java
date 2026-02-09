package com.concordium.sdk.transactions;

import lombok.*;

import java.nio.ByteBuffer;

import static com.google.common.primitives.Bytes.concat;

/**
 * Account transaction payload.
 */
@EqualsAndHashCode
@ToString
@Getter
public abstract class Payload {

    /**
     * Type of the payload (account transaction).
     */
    private final TransactionType type;

    Payload(@NonNull TransactionType type) {
        this.type = type;
    }

    /**
     * Get the serialized payload.
     * The actual payload is prepended with a tag (a byte) which
     * indicates the {@link TransactionType}
     *
     * @return the serialized payload (including the type tag).
     */
    public final byte[] getBytes() {
        return concat(
                new byte[]{type.getValue()},
                getPayloadBytes()
        );
    }

    /**
     * This must return the payload only.
     * The type will be prepended by {@link Payload#getBytes()}
     *
     * @return the raw serialized payload.
     */
    protected abstract byte[] getPayloadBytes();

    /**
     * @param source a buffer to read payload bytes from, including {@link TransactionType} byte.
     * @return deserialized {@link Payload}
     * @throws UnsupportedOperationException if payload can't be read.
     *                                       Not all payload types can be read.
     *                                       Use {@link RawPayload} for unsupported types.
     */
    public static Payload fromBytes(ByteBuffer source) {
        val payloadType = TransactionType.parse(source.get());
        switch (payloadType) {
            case UPDATE_SMART_CONTRACT_INSTANCE:
                return UpdateContract.fromBytes(source);
            case SIMPLE_TRANSFER:
                return Transfer.fromBytes(source);
            case REGISTER_DATA:
                return RegisterData.fromBytes(source);
            case TRANSFER_WITH_MEMO:
                return TransferWithMemo.fromBytes(source);
            case CONFIGURE_DELEGATION:
                return ConfigureDelegation.fromBytes(source);
            case TOKEN_UPDATE:
                return TokenUpdate.fromBytes(source);
            default:
                throw new UnsupportedOperationException("Unsupported payload type: " + payloadType + ". Use RawPayload instead");
        }
    }
}
