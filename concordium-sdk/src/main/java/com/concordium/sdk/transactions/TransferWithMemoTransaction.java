package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public class TransferWithMemoTransaction extends AbstractTransaction {
    private final AccountAddress receiver;
    private final CCDAmount amount;
    private final Memo memo;

    private final AccountAddress sender;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;

    private BlockItem blockItem;

    @Builder
    TransferWithMemoTransaction(AccountAddress sender, AccountAddress receiver, CCDAmount amount, Memo memo, AccountNonce nonce, Expiry expiry, TransactionSigner signer) throws TransactionCreationException {
        this.receiver = receiver;
        this.amount = amount;
        this.memo = memo;
        this.nonce = nonce;
        this.sender = sender;
        this.expiry = expiry;
        this.signer = signer;
    }

    @Override
    BlockItem getBlockItem() {
        return blockItem;
    }

    public static TransferWithMemoTransactionBuilder builder() {
        return new CustomBuilder();
    }

    private static class CustomBuilder extends TransferWithMemoTransactionBuilder {
        @Override
        public TransferWithMemoTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            Transaction.verifyTransferInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.receiver, transaction.amount, transaction.signer);
            if (Objects.isNull(transaction.memo)) {
                throw TransactionCreationException.from(new IllegalArgumentException("Memo cannot be null"));
            }
            transaction.blockItem = createNewTransaction(transaction).toBlockItem();
            return transaction;
        }

        private Payload createNewTransaction(TransferWithMemoTransaction transaction) throws TransactionCreationException {
            return TransferWithMemo.createNew(transaction.getReceiver(),
                            transaction.getAmount(),
                            transaction.getMemo())
                    .withHeader(TransactionHeader.builder()
                            .sender(transaction.getSender())
                            .accountNonce(transaction.getNonce().getNonce())
                            .expiry(transaction.getExpiry().getValue())
                            .build())
                    .signWith(transaction.getSigner());
        }
    }
}

