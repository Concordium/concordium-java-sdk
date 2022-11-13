package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

/**
 * Construct a transaction to transfer an amount with schedule and memo.
 */
@Getter
public class TransferScheduleWithMemoTransaction extends AbstractTransaction {
    /**
     * The account address of the recepient.
     */
    private final AccountAddress to;
    /**
     * The release schedule. This can be at most 255 elements.
     */
    private final Schedule[] schedule;
    /**
     * The data that was registered on the chain.
     */
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

    /**
     * Maximum energy **allowed** for the transaction to use.
     */
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

    /**
     * Verify that the input parameters are valid for a transfer schedule transaction with a memo.
     */
    static void verifyTransferScheduleWithMemoInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, AccountAddress to, Schedule[] schedule, TransactionSigner signer, Memo memo) throws TransactionCreationException {
        TransferScheduleTransaction.verifyTransferScheduleInput(sender, nonce, expiry, to, schedule, signer);
        if (Objects.isNull(memo)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Memo cannot be null"));
        }
    }

    /**
     * This function returns the block item associated with this block.
     */
    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends TransferScheduleWithMemoTransaction.TransferScheduleWithMemoTransactionBuilder {
        @Override
        public TransferScheduleWithMemoTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyTransferScheduleWithMemoInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.to, transaction.schedule, transaction.signer, transaction.memo);
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
