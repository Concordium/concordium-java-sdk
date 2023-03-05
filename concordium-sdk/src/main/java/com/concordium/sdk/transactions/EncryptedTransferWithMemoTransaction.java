package com.concordium.sdk.transactions;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class EncryptedTransferWithMemoTransaction extends AbstractTransaction {

    /**
     * A constructor of {@link EncryptedTransferWithMemoTransaction} class.
     */
    @Builder
    public EncryptedTransferWithMemoTransaction(
            @NonNull final EncryptedAmountTransferData data,
            @NonNull final AccountAddress receiver,
            @NonNull final Memo memo,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, EncryptedTransferWithMemo.createNew(data, receiver, memo));
    }
}
