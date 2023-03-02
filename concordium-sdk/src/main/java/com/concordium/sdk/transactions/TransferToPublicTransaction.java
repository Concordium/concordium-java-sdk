package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Construct a transaction transfer the given amount from encrypted
 * to public balance of the given account.
 */
@Getter
public class TransferToPublicTransaction extends AbstractAccountTransaction {

    /**
     * A constructor of {@link TransferToPublicTransaction} class.
     */
    @Builder
    public TransferToPublicTransaction(
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
}
