package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

/**
 * Construct a transaction to transfer from public to encrypted balance of the sender account.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferToEncryptedTransaction extends AccountTransaction {
    private TransferToEncryptedTransaction(
            @NonNull final CCDAmount amount,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferToEncrypted.createNew(amount), TransactionTypeCost.TRANSFER_TO_ENCRYPTED.getValue());
    }

    /**
     * Creates {@link TransferToEncryptedTransaction}.
     *
     * @param amount {@link CCDAmount} to Encrypt.
     * @param sender Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce  Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry {@link Expiry} of this transaction.
     * @param signer {@link Signer} of this transaction.
     * @return Initialized {@link TransferToEncryptedTransaction}.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder
    public static TransferToEncryptedTransaction from(
            final CCDAmount amount,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new TransferToEncryptedTransaction(amount, sender, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
