package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class EncryptedTransferWithMemoTransaction extends AbstractAccountTransaction {

    /**
     * A constructor of {@link EncryptedTransferWithMemoTransaction} class.
     */
    private EncryptedTransferWithMemoTransaction(
            @NonNull final EncryptedAmountTransferData data,
            @NonNull final AccountAddress receiver,
            @NonNull final Memo memo,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, EncryptedTransferWithMemo.createNew(data, receiver, memo));
    }

    @Builder
    public static EncryptedTransferWithMemoTransaction from(
            final EncryptedAmountTransferData data,
            final AccountAddress receiver,
            final Memo memo,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new EncryptedTransferWithMemoTransaction(data, receiver, memo, sender, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
