package com.concordium.sdk.transactions;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Construct a transaction to transfer from public to encrypted balance of the sender account.
 */
@Getter
public class TransferToEncryptedTransaction extends AbstractTransaction {
    @Builder
    public TransferToEncryptedTransaction(
            @NonNull final CCDAmount amount,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferToEncrypted.createNew(amount));
    }
}
