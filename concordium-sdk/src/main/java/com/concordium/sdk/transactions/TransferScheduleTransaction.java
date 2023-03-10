package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.*;


/**
 * Construct a transaction to transfer an amount with schedule.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferScheduleTransaction extends AbstractAccountTransaction {

    private TransferScheduleTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress to,
            @NonNull final Schedule[] schedule,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferSchedule.createNew(to, schedule));
    }

    @Builder
    public static TransferScheduleTransaction from(
            final AccountAddress sender,
            final AccountAddress to,
            final Schedule[] schedule,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new TransferScheduleTransaction(sender, to, schedule, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
