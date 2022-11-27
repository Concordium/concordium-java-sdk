package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

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

    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

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

    public static TransferToPublicTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends TransferToPublicTransactionBuilder {
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
