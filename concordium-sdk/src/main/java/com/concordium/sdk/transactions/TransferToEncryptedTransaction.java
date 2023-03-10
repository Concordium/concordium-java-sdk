package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.*;

/**
 * Construct a transaction to transfer from public to encrypted balance of the sender account.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferToEncryptedTransaction extends AbstractAccountTransaction {
    private TransferToEncryptedTransaction(
            @NonNull final CCDAmount amount,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferToEncrypted.createNew(amount));
    }

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
