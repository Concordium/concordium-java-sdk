package com.concordium.sdk.transactions;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A class that represents a ConfigureDelegationTransaction.
 * This transaction is used to register the account as a delegator.
 */
@Getter
public class ConfigureDelegationTransaction extends AbstractTransaction {
    @Builder
    public ConfigureDelegationTransaction(
            @NonNull final ConfigureDelegationPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, ConfigureDelegation.createNew(payload));
    }
}
