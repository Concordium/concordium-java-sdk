package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A class that represents a ConfigureBakerTransaction.
 * This transaction is used to register the account as a baker.
 */
@Getter
public class ConfigureBakerTransaction extends AbstractTransaction {

    ConfigureBakerTransaction(
            @NonNull final ConfigureBakerPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, ConfigureBaker.createNew(payload));
    }

    /**
     * Creates new {@link ConfigureBakerTransaction}
     *
     * @param payload Whether to add earnings to the stake automatically or not.
     * @param sender  Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce   Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry  {@link Expiry} of this transaction.
     * @param signer  {@link Signer} of this transaction.
     * @return Instance of {@link ConfigureBakerTransaction}
     */
    @Builder
    public static ConfigureBakerTransaction from(@NonNull final ConfigureBakerPayload payload,
                                                 @NonNull final AccountAddress sender,
                                                 @NonNull final AccountNonce nonce,
                                                 @NonNull final Expiry expiry,
                                                 @NonNull final TransactionSigner signer) {
        try {
            return new ConfigureBakerTransaction(payload, sender, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
