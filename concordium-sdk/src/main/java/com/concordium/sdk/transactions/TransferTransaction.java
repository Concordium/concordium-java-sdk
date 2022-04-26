package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

@Getter
public class TransferTransaction extends AbstractTransaction {
    private final AccountAddress sender;
    private final AccountAddress receiver;
    private final GTUAmount amount;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;

    private BlockItem blockItem;

    @Builder
    public TransferTransaction(AccountAddress sender,
                               AccountAddress receiver,
                               GTUAmount amount,
                               AccountNonce nonce,
                               Expiry expiry,
                               TransactionSigner signer) throws TransactionCreationException {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
    }

    public static TransferTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends TransferTransaction.TransferTransactionBuilder {
        @Override
        public TransferTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            Transaction.verifyTransferInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.receiver, transaction.amount, transaction.signer);
            transaction.blockItem = createSimpleTransfer(transaction).toBlockItem();
            return transaction;
        }

        private Payload createSimpleTransfer(TransferTransaction transaction) throws TransactionCreationException {
            return Transfer.createNew(
                            transaction.receiver,
                            transaction.amount).
                    withHeader(TransactionHeader.builder()
                            .sender(transaction.sender)
                            .accountNonce(transaction.nonce.getNonce())
                            .expiry(transaction.expiry.getValue())
                            .build())
                    .signWith(transaction.signer);
        }
    }
}
