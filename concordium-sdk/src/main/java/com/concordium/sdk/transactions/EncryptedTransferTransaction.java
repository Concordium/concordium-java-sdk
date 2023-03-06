package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class EncryptedTransferTransaction extends AbstractTransaction {

    /**
     * A constructor of {@link EncryptedTransferTransaction} class.
     */
    private EncryptedTransferTransaction(
            @NonNull final EncryptedAmountTransferData data,
            @NonNull final AccountAddress receiver,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, EncryptedTransfer.createNew(data, receiver));
    }

    @Builder
    public static EncryptedTransferTransaction from(final EncryptedAmountTransferData data,
                                                    final AccountAddress receiver,
                                                    final AccountAddress sender,
                                                    final AccountNonce nonce,
                                                    final Expiry expiry,
                                                    final TransactionSigner signer) {
        try {
            return new EncryptedTransferTransaction(data, receiver, sender, nonce, expiry, signer);
        } catch (NullPointerException ex) {
            throw TransactionCreationException.from(ex);
        }
    }
}
