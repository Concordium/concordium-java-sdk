package com.concordium.sdk.transactions;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;


/**
 * Construct a transaction to transfer an amount with schedule.
 */
@Getter
public class TransferScheduleTransaction extends AbstractTransaction {

    @Builder
    public TransferScheduleTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress to,
            @NonNull final Schedule[] schedule,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferSchedule.createNew(to, schedule));
    }
}
