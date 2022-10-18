package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

/**
 * Construct a transaction to transfer from public to encrypted balance of the sender account.
 */
@Getter
public class TransferToEncryptedTransaction extends AbstractTransaction {

    private final CCDAmount amount;
    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;

    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

    @Builder
    public TransferToEncryptedTransaction(CCDAmount amount, AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, BlockItem blockItem, UInt64 maxEnergyCost) throws TransactionCreationException {
        this.amount = amount;
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
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
                            transaction.amount,
                            transaction.maxEnergyCost).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }

        static void verifyTransferToEncryptedInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, CCDAmount amount) throws TransactionCreationException {
            Transaction.verifyCommonInput(sender, nonce, expiry, signer);

            if (Objects.isNull(amount)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
            }
        }
    }
}
