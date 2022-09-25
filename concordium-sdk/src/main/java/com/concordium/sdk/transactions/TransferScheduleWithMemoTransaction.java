package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

@Getter
public class TransferScheduleWithMemoTransaction extends AbstractTransaction {
    private final AccountAddress sender;
    private final AccountAddress to;
    private final Schedule[] schedule;

    private final Memo memo;
    private final AccountNonce nonce;
    private final Expiry expiry;
    private final TransactionSigner signer;
    private final UInt64 maxEnergyCost;

    private BlockItem blockItem;

    @Builder
    public TransferScheduleWithMemoTransaction(AccountAddress sender, AccountAddress to, Schedule[] schedule, Memo memo, AccountNonce nonce, Expiry expiry, TransactionSigner signer, UInt64 maxEnergyCost) throws TransactionCreationException {
        this.sender = sender;
        this.to = to;
        this.schedule = schedule;
        this.memo = memo;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.maxEnergyCost = maxEnergyCost;
    }

    public static TransferScheduleWithMemoTransactionBuilder builder() {
        return new CustomBuilder();
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends TransferScheduleWithMemoTransaction.TransferScheduleWithMemoTransactionBuilder {
        @Override
        public TransferScheduleWithMemoTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            Transaction.verifyTransferScheduleWithMemoInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.to, transaction.schedule, transaction.signer, transaction.memo);
            transaction.blockItem = createSimpleTransfer(transaction).toBlockItem();
            return transaction;
        }

        private Payload createSimpleTransfer(TransferScheduleWithMemoTransaction transaction) throws TransactionCreationException {
            return TransferScheduleWithMemo.createNew(
                            transaction.to,
                            transaction.schedule,
                            transaction.memo,
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
