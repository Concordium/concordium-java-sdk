package com.concordium.sdk.transactions;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class RegisterDataTransaction extends AbstractTransaction {
    @Builder
    public RegisterDataTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final Data data,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, RegisterData.createNew(data));
    }
}
