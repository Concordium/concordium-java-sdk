package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import lombok.*;


/**
 * Construct a transaction to transfer an amount with schedule.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferScheduleTransaction extends AccountTransaction {

    private TransferScheduleTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress to,
            @NonNull final Schedule[] schedule,
            @NonNull final Nonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferSchedule.createNew(to, schedule),getCost(schedule));
    }

    private static UInt64 getCost(@NonNull Schedule[] schedule) {
        UInt16 scheduleLen = UInt16.from(schedule.length);
        val maxEnergyCost = UInt64.from(scheduleLen.getValue()).getValue() * (300 + 64);
        return UInt64.from(maxEnergyCost);
    }

    /**
     * @param to       Reciever {@link AccountAddress} of the Scheduled Transfer.
     * @param schedule {@link Schedule} of the Transfer.
     * @param sender   Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce    Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry   {@link Expiry} of this transaction.
     * @param signer   {@link Signer} of this transaction.
     * @return Initialized {@link TransferScheduleTransaction}.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder
    public static TransferScheduleTransaction from(
            final AccountAddress sender,
            final AccountAddress to,
            final Schedule[] schedule,
            final Nonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new TransferScheduleTransaction(sender, to, schedule, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
