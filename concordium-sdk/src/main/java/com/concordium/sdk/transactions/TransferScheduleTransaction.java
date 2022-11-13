package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;


/**
 * Construct a transaction to transfer an amount with schedule.
 */
@Getter
public class TransferScheduleTransaction extends AbstractTransaction {
    /**
     * The account address of the recepient.
     */
    private final AccountAddress to;
    /**
     * The release schedule. This can be at most 255 elements.
     */
    private final Schedule[] schedule;

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
    public TransferScheduleTransaction(AccountAddress sender, AccountAddress to, Schedule[] schedule, AccountNonce nonce, Expiry expiry, TransactionSigner signer, BlockItem blockItem, UInt64 maxEnergyCost) throws TransactionCreationException {
        this.sender = sender;
        this.to = to;
        this.schedule = schedule;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;
        this.blockItem = blockItem;
        this.maxEnergyCost = maxEnergyCost;
    }

    public static TransferScheduleTransactionBuilder builder() {
        return new CustomBuilder();
    }

    /**
     * Verify that the input parameters are valid for a transfer schedule transaction.
     */
    static void verifyTransferScheduleInput(AccountAddress sender, AccountNonce nonce, Expiry expiry, AccountAddress to, Schedule[] schedule, TransactionSigner signer) throws TransactionCreationException {
        Transaction.verifyAccountTransactionHeaders(sender, nonce, expiry, signer);
        if (Objects.isNull(to)) {
            throw TransactionCreationException.from(new IllegalArgumentException("To cannot be null"));
        }
        if (Objects.isNull(schedule)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Schedule cannot be null"));
        }
        if (schedule.length > 255) {
            throw TransactionCreationException.from(new IllegalArgumentException("Schedule size can be maximum 255"));
        }
    }

    /**
     * This function returns the block item associated with this block.
     */
    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    private static class CustomBuilder extends TransferScheduleTransaction.TransferScheduleTransactionBuilder {
        @Override
        public TransferScheduleTransaction build() throws TransactionCreationException {
            val transaction = super.build();
            verifyTransferScheduleInput(transaction.sender, transaction.nonce, transaction.expiry, transaction.to, transaction.schedule, transaction.signer);
            transaction.blockItem = createSimpleTransfer(transaction).toBlockItem();
            return transaction;
        }

        private Payload createSimpleTransfer(TransferScheduleTransaction transaction) throws TransactionCreationException {
            return TransferSchedule.createNew(
                            transaction.to,
                            transaction.schedule,
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
