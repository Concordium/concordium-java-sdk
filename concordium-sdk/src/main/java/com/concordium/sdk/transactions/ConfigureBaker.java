package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;


/**
 * Payload of the `AddBaker` transaction. This transaction registers the
 * account as a baker.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@ToString
public final class ConfigureBaker extends Payload {

    /**
     * The keys with which the baker registered.
     */
    private final ConfigureBakerPayload payload;

    private final UInt64 maxEnergyCost;

    @Override
    public PayloadType getType() {
        return PayloadType.CONFIGURE_BAKER;
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    public byte[] getBytes() {
        val payloadBytes = payload.getBytes();
        val bufferLength = TransactionType.BYTES +
                payloadBytes.length;

        val buffer = ByteBuffer.allocate(bufferLength);

        buffer.put(TransactionType.CONFIGURE_BAKER.getValue());
        buffer.put(payloadBytes);
        return buffer.array();
    }

    static ConfigureBaker createNew(ConfigureBakerPayload payload, UInt64 maxEnergyCost) {
        return new ConfigureBaker(
                payload,
                maxEnergyCost
        );
    }

}
