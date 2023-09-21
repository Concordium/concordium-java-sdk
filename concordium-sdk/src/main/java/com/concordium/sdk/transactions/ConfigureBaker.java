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

    /**
     * Get the cost of the transaction based on the keys with proofs
     *
     * @return UInt64
     */
    @Override
    protected UInt64 getTransactionTypeCost() {
        if (this.payload.getKeysWithProofs() != null)
            return TransactionTypeCost.CONFIGURE_BAKER_WITH_PROOFS.getValue();
        else
            return TransactionTypeCost.CONFIGURE_BAKER.getValue();
    }

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
