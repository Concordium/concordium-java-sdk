package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A class that represents a ConfigureDelegationTransaction.
 * This transaction is used to register the account as a delegator.
 */
@Getter
public class ConfigureDelegationTransaction extends AbstractAccountTransaction {
    private ConfigureDelegationTransaction(
            @NonNull final ConfigureDelegationPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, ConfigureDelegation.createNew(payload));
    }

    @Builder
    public static ConfigureDelegationTransaction from(final ConfigureDelegationPayload payload,
                                                      final AccountAddress sender,
                                                      final AccountNonce nonce,
                                                      final Expiry expiry,
                                                      final TransactionSigner signer) {
        try {
            return new ConfigureDelegationTransaction(payload, sender, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
