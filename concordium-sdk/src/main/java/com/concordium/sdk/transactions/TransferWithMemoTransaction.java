package com.concordium.sdk.transactions;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class TransferWithMemoTransaction extends AbstractAccountTransaction {
    @Builder
    public TransferWithMemoTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress receiver,
            @NonNull final CCDAmount amount,
            @NonNull final Memo memo,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferWithMemo.createNew(receiver, amount, memo));
    }
}

