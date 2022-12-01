package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

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
     * Account Address of the sender.
     */
    private final AccountAddress sender;
    /**
     * The senders account next available nonce.
     */
    private final AccountNonce nonce;
    /**
     * Indicates when the transaction should expire.
     */
    private final Expiry expiry;
    /**
     * A signer object that is used to sign the transaction.
     */
    private final TransactionSigner signer;

    /**
     * Maximum energy **allowed** for the transaction to use.
     */
    private final UInt64 maxEnergyCost;
    private BlockItem blockItem;


    /**
     * A constructor of {@link TransferToPublicTransaction} class.
     */
    @Builder
    public TransferToPublicTransaction(
            EncryptedAmount remainingAmount,
            CCDAmount transferAmount,
            UInt64 index,
            SecToPubAmountTransferProof proof,
            AccountAddress sender,
            AccountNonce nonce,
            Expiry expiry,
            TransactionSigner signer,
            BlockItem blockItem,
            UInt64 maxEnergyCost) {
        this.remainingAmount = remainingAmount;
        this.transferAmount = transferAmount;
        this.index = index;
        this.proof = proof;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * @return A new instance of the {@link TransferToPublicTransactionBuilder}  class.
     */
    public static TransferToPublicTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends TransferToPublicTransactionBuilder {

        // Overriding the build method of the super class.
        @Override
        public TransferToPublicTransaction build() {
            val transaction = super.build();

            try {
                verifyTransferToPublicInput(
                        transaction.sender,
                        transaction.nonce,
                        transaction.expiry,
                        transaction.signer,
                        transaction.remainingAmount,
                        transaction.transferAmount,
                        transaction.index,
                        transaction.proof);
                transaction.blockItem = transferToPublicInstance(transaction).toBlockItem();
            } catch (TransactionCreationException e) {
                throw new RuntimeException(e);
            }

            return transaction;
        }

        private Payload transferToPublicInstance(TransferToPublicTransaction transaction) throws TransactionCreationException {
            return TransferToPublic.createNew(
                            transaction.remainingAmount,
                            transaction.transferAmount,
                            transaction.index,
                            transaction.proof,
                            transaction.maxEnergyCost).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyTransferToPublicInput(
                AccountAddress sender,
                AccountNonce nonce,
                Expiry expiry,
                TransactionSigner signer,
                EncryptedAmount remainingAmount,
                CCDAmount transferAmount,
                UInt64 index,
                SecToPubAmountTransferProof proof) throws TransactionCreationException {

            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);

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
        }
    }
}
