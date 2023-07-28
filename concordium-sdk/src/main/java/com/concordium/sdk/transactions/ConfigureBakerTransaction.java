package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

/**
 * A class that represents a ConfigureBakerTransaction.
 * This transaction is used to register the account as a baker.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConfigureBakerTransaction extends AccountTransaction {

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
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder
    public static ConfigureBakerTransaction from(final ConfigureBakerPayload payload,
                                                 final AccountAddress sender,
                                                 final AccountNonce nonce,
                                                 final Expiry expiry,
                                                 final TransactionSigner signer) {
        try {
            return new ConfigureBakerTransaction(payload, sender, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
