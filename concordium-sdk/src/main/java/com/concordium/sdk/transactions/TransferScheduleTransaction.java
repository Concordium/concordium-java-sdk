package com.concordium.sdk.transactions;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;


/**
 * Construct a transaction to transfer an amount with schedule.
 */
@Getter
public class TransferScheduleTransaction extends AbstractAccountTransaction {

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
