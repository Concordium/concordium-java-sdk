package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Construct a transaction to transfer an amount with schedule and memo.
 */
@Getter
public class TransferScheduleWithMemoTransaction extends AbstractAccountTransaction {
    @Builder
    public TransferScheduleWithMemoTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress to,
            @NonNull final Schedule[] schedule,
            @NonNull final Memo memo,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferScheduleWithMemo.createNew(to, schedule, memo));

        if (schedule.length > 255) {
            throw TransactionCreationException.from(new IllegalArgumentException("Schedule size can be maximum 255"));
        }
    }
}
