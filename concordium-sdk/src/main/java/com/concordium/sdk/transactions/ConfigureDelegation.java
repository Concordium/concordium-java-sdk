package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;


/**
 * Payload of the `ConfigureDelegation` transaction. This transaction
 * configure an account's stake delegation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@ToString
public final class ConfigureDelegation extends Payload {

    /**
     * The payload for configuring delegation.
     */
    private final ConfigureDelegationPayload payload;

    @Override
    public PayloadType getType() {
        return PayloadType.CONFIGURE_DELEGATION;
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return UInt64.from(300);
    }

    public byte[] getBytes() {
        val payloadBytes = payload.getBytes();
        val bufferLength = TransactionType.BYTES +
                payloadBytes.length;

        val buffer = ByteBuffer.allocate(bufferLength);

        buffer.put(TransactionType.CONFIGURE_DELEGATION.getValue());
        buffer.put(payloadBytes);
        return buffer.array();
    }

    static ConfigureDelegation createNew(ConfigureDelegationPayload payload) {
        return new ConfigureDelegation(payload);
    }

}
