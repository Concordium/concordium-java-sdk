package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class RegisterDataTransaction extends AbstractAccountTransaction {
    private RegisterDataTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final Data data,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, RegisterData.createNew(data));
    }

    private RegisterDataTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull Data payload) {
        super(header,
                signature,
                TransactionType.REGISTER_DATA,
                payload.getBytes());
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

    @Builder(builderMethodName = "builderBlockItem")
    public static RegisterDataTransaction from(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull Data payload) {
        try {
            return new RegisterDataTransaction(header, signature, payload);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
