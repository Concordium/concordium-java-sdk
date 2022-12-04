package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public class EncryptedTransferWithMemoTransaction extends AbstractTransaction {

    /**
     *  Data that will go onto an encrypted amount transfer.
     */
    private final EncryptedAmountTransferData data;

    /**
     * Account Address of the sender.
     */
    private final AccountAddress to;
    private final Memo memo;

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

    /**
     * A constructor of {@link EncryptedTransferWithMemoTransaction} class.
     */
    @Builder
    public EncryptedTransferWithMemoTransaction(
            EncryptedAmountTransferData data,
            AccountAddress to, Memo memo, AccountAddress sender,
            AccountNonce nonce,
            Expiry expiry,
            TransactionSigner signer,
            BlockItem blockItem) throws TransactionCreationException {
        this.data = data;
        this.to = to;
        this.memo = memo;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
    }

    /**
     * @return A new instance of the {@link EncryptedTransferWithMemoTransaction}  class.
     */
    public static EncryptedTransferWithMemoTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends EncryptedTransferWithMemoTransactionBuilder {
        @Override
        public EncryptedTransferWithMemoTransaction build() throws TransactionCreationException {
            val transaction = super.build();

            verifyEncryptedTransferInput(
                    transaction.sender,
                    transaction.nonce,
                    transaction.expiry,
                    transaction.signer,
                    transaction.to,
                    transaction.data,
                    transaction.memo);
            transaction.blockItem = EncryptedTransferWithMemoInstance(transaction).toBlockItem();
            return transaction;
        }

        private Payload EncryptedTransferWithMemoInstance(EncryptedTransferWithMemoTransaction transaction) throws TransactionCreationException {
            return EncryptedTransferWithMemo.createNew(
                            transaction.data,
                            transaction.to,
                            transaction.memo).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }


        static void verifyEncryptedTransferInput(
                AccountAddress sender,
                AccountNonce nonce,
                Expiry expiry,
                TransactionSigner signer,
                AccountAddress to,
                EncryptedAmountTransferData data,
                Memo memo) throws TransactionCreationException {

            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);

            if (Objects.isNull(to)) {
                throw TransactionCreationException.from(new IllegalArgumentException("To address cannot be null"));
            }
            if (Objects.isNull(memo)) {
                throw TransactionCreationException.from(new IllegalArgumentException("To address cannot be null"));
            }
            if (Objects.isNull(data)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
            }
        }
    }
}
