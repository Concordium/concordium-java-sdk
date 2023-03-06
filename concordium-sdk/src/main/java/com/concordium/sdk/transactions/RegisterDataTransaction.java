package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class RegisterDataTransaction extends AbstractTransaction {
    RegisterDataTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final Data data,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, RegisterData.createNew(data));
    }

    @Builder
    public static RegisterDataTransaction from(
            final AccountAddress sender,
            final Data data,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new RegisterDataTransaction(sender, data, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
