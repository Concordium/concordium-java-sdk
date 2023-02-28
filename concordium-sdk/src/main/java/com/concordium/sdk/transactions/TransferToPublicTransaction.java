package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * Construct a transaction transfer the given amount from encrypted
 * to public balance of the given account.
 */
@Getter
public class TransferToPublicTransaction extends AbstractTransaction {

    /**
     * Encryption of the remaining amount.
     */
    private final EncryptedAmount remainingAmount;

    /**
     * Amount that will be sent.
     */
    private final CCDAmount transferAmount;

    /**
     * The index such that the encrypted amount used in the transfer represents
     * the aggregate of all encrypted amounts with indices < `index` existing
     * on the account at the time. New encrypted amounts can only add new indices.
     */
    private final UInt64 index;

    /**
     * A collection of all the proofs.
     */
    private final SecToPubAmountTransferProof proof;


    /**
     * A constructor of {@link TransferToPublicTransaction} class.
     */
    @Builder
    public TransferToPublicTransaction(
            final EncryptedAmount remainingAmount,
            final CCDAmount transferAmount,
            final UInt64 index,
            final SecToPubAmountTransferProof proof,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(remainingAmount)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Remaining Amount cannot be null"));
        }
        if (Objects.isNull(transferAmount)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Transfer Amount cannot be null"));
        }
        if (Objects.isNull(index)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Index cannot be null"));
        }
        if (Objects.isNull(proof)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Proof cannot be null"));
        }
        this.remainingAmount = remainingAmount;
        this.transferAmount = transferAmount;
        this.index = index;
        this.proof = proof;
    }

    @Override
    public BlockItem getBlockItem() {
        return TransferToPublic.createNew(
                        getRemainingAmount(),
                        getTransferAmount(),
                        getIndex(),
                        getProof())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
