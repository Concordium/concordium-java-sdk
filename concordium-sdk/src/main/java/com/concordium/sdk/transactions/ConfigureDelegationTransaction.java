package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

/**
 * A class that represents a ConfigureDelegationTransaction.
 * This transaction is used to register the account as a delegator.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConfigureDelegationTransaction extends AccountTransaction {
    private ConfigureDelegationTransaction(
            @NonNull final ConfigureDelegationPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, ConfigureDelegation.createNew(payload));
    }

    /**
     * Creates new {@link ConfigureDelegationTransaction}.
     *
     * @param payload Payload for this Transaction.
     * @param sender  Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce   Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry  {@link Expiry} of this transaction.
     * @param signer  {@link Signer} of this transaction.
     * @return Initialized {@link ConfigureDelegationTransaction}
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     * Ex when any of the input param is NULL.
     */
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
