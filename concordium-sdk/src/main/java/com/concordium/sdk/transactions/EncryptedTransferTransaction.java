package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public class EncryptedTransferTransaction extends AbstractTransaction {

    /**
     *  Data that will go onto an encrypted amount transfer.
     */
    private final EncryptedAmountTransferData data;
    /**
     * The account address to which the transfer will be sent.
     */
    private final AccountAddress to;

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
     * A constructor of {@link EncryptedTransferTransaction} class.
     */
    @Builder
    public EncryptedTransferTransaction(
            EncryptedAmountTransferData data,
            AccountAddress to, AccountAddress sender,
            AccountNonce nonce,
            Expiry expiry,
            TransactionSigner signer,
            BlockItem blockItem,
            UInt64 maxEnergyCost) throws TransactionCreationException {
        this.data = data;
        this.to = to;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * @return A new instance of the {@link EncryptedTransferTransaction}  class.
     */
    public static EncryptedTransferTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends EncryptedTransferTransactionBuilder {
        @Override
        public EncryptedTransferTransaction build() throws TransactionCreationException {
            val transaction = super.build();

            verifyEncryptedTransferInput(
                    transaction.sender,
                    transaction.nonce,
                    transaction.expiry,
                    transaction.signer,
                    transaction.to,
                    transaction.data);
            transaction.blockItem = EncryptedTransferInstance(transaction).toBlockItem();
            return transaction;
        }

        private Payload EncryptedTransferInstance(EncryptedTransferTransaction transaction) throws TransactionCreationException {
            return EncryptedTransfer.createNew(
                            transaction.data,
                            transaction.to,
                            transaction.maxEnergyCost).
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
                EncryptedAmountTransferData data) throws TransactionCreationException {

            Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);

            if (Objects.isNull(to)) {
                throw TransactionCreationException.from(new IllegalArgumentException("To address cannot be null"));
            }
            if (Objects.isNull(data)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
            }
        }
    }
}
