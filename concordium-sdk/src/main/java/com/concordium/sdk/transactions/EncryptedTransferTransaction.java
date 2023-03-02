package com.concordium.sdk.transactions;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class EncryptedTransferTransaction extends AbstractAccountTransaction {

    /**
     * A constructor of {@link EncryptedTransferTransaction} class.
     */
    @Builder
    public EncryptedTransferTransaction(
            @NonNull final EncryptedAmountTransferData data,
            @NonNull final AccountAddress receiver,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, EncryptedTransfer.createNew(data, receiver));
    }
}
