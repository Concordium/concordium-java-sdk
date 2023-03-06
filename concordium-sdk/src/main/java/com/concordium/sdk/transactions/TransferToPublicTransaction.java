package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Construct a transaction transfer the given amount from encrypted
 * to public balance of the given account.
 */
@Getter
public class TransferToPublicTransaction extends AbstractTransaction {

    /**
     * A constructor of {@link TransferToPublicTransaction} class.
     */
    @Builder
    private TransferToPublicTransaction(
            @NonNull final EncryptedAmount remainingAmount,
            @NonNull final CCDAmount transferAmount,
            @NonNull final UInt64 index,
            @NonNull final SecToPubAmountTransferProof proof,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferToPublic.createNew(
                remainingAmount,
                transferAmount,
                index,
                proof));
    }

    public static TransferToPublicTransaction from(
            final EncryptedAmount remainingAmount,
            final CCDAmount transferAmount,
            final UInt64 index,
            final SecToPubAmountTransferProof proof,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new TransferToPublicTransaction(remainingAmount,
                    transferAmount,
                    index,
                    proof,
                    sender,
                    nonce,
                    expiry,
                    signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
