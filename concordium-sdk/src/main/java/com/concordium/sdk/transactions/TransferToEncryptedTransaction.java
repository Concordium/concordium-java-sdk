package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

@Getter
public class TransferToEncryptedTransaction extends AbstractTransaction {

    private final TransferToEncryptedPayload payload;
    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;

    private BlockItem blockItem;
    private final UInt64 maxEnergyCost;

    @Builder
    public TransferToEncryptedTransaction(TransferToEncryptedPayload payload, AccountAddress sender, AccountNonce nonce, Expiry expiry, TransactionSigner signer, BlockItem blockItem, UInt64 maxEnergyCost) {
        this.payload = payload;
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
        public TransferToEncryptedTransaction build() {
            val transaction = super.build();
            try {
                Transaction.verifyTransferToEncryptedInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.signer, transaction.payload);
            } catch (TransactionCreationException e) {
                throw new RuntimeException(e);
            }
            try {
                transaction.blockItem = transferToEncryptedInstance(transaction).toBlockItem();
            } catch (TransactionCreationException e) {
                throw new RuntimeException(e);
            }
            return transaction;
        }


        private Payload transferToEncryptedInstance(TransferToEncryptedTransaction transaction) throws TransactionCreationException {
            return TransferToEncrypted.createNew(
                            transaction.payload,
                            transaction.maxEnergyCost).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }
    }
}
