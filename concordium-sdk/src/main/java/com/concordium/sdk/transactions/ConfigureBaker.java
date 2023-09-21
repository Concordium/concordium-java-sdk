package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Payload of the `ConfigureBaker` transaction. This transaction contains the keys
 * with which the baker registered.
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


    @Override
    public TransactionType getTransactionType() {
        return TransactionType.CONFIGURE_BAKER;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return payload.getBytes();
    }

    static ConfigureBaker createNew(ConfigureBakerPayload payload) {
        return new ConfigureBaker(payload);
    }
}
