package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


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
    public TransactionType getTransactionType() {
        return TransactionType.CONFIGURE_DELEGATION;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return payload.getBytes();
    }

    static ConfigureDelegation createNew(ConfigureDelegationPayload payload) {
        return new ConfigureDelegation(payload);
    }

}
