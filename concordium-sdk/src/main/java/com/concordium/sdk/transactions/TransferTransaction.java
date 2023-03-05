package com.concordium.sdk.transactions;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class TransferTransaction extends AbstractTransaction {
    @Builder
    public TransferTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress receiver,
            @NonNull final CCDAmount amount,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, Transfer.createNew(receiver, amount));
    }
}
