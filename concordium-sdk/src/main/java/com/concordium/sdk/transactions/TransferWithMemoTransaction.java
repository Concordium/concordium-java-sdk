package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class TransferWithMemoTransaction extends AbstractTransaction {
    private TransferWithMemoTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress receiver,
            @NonNull final CCDAmount amount,
            @NonNull final Memo memo,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferWithMemo.createNew(receiver, amount, memo));
    }

    @Builder
    public static TransferWithMemoTransaction from(
            final AccountAddress sender,
            final AccountAddress receiver,
            final CCDAmount amount,
            final Memo memo,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new TransferWithMemoTransaction(sender, receiver, amount, memo, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}

