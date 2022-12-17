package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

/**
 * Construct a transaction to transfer from public to encrypted balance of the sender account.
 */
@Getter
public class TransferToEncryptedTransaction extends AbstractTransaction {
    /**
     * The amount to transfer from public to encrypted balance of the sender account.
     */
    private final CCDAmount amount;

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

    private BlockItem blockItem;

    @Builder
    public TransferToEncryptedTransaction(CCDAmount amount, AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, BlockItem blockItem) throws TransactionCreationException {
        this.amount = amount;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
    }

    public static TransferToEncryptedTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends TransferToEncryptedTransactionBuilder {
        @Override
        public TransferToEncryptedTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyTransferToEncryptedInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.signer, transaction.amount);
            transaction.blockItem = transferToEncryptedInstance(transaction).toBlockItem();
            return transaction;
        }


        private Payload transferToEncryptedInstance(TransferToEncryptedTransaction transaction) throws TransactionCreationException {
            return TransferToEncrypted.createNew(
                            transaction.amount).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyTransferToEncryptedInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, CCDAmount amount) throws TransactionCreationException {
            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);

            if (Objects.isNull(amount)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Amount cannot be null"));
            }
        }
    }
}
