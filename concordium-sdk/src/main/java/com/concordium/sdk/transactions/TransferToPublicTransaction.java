package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * Construct a transaction transfer the given amount from encrypted
 * to public balance of the given account.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferToPublicTransaction extends AccountTransaction {

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
                proof), TransactionTypeCost.TRANSFER_TO_PUBLIC.getValue());
    }

    /**
     * @param remainingAmount {@link EncryptedAmount} which remains after input amount is made public / decrypted.
     * @param transferAmount  {@link CCDAmount} which is being made public / decrypted,
     * @param index           The index such that the encrypted amount used in the transfer represents
     *                        the aggregate of all encrypted amounts with indices < `index` existing
     *                        on the account at the time. New encrypted amounts can only add new indices.
     * @param proof           A collection of all the proofs.
     * @param sender          Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce           Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry          {@link Expiry} of this transaction.
     * @param signer          {@link Signer} of this transaction.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
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
