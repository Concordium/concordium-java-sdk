package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

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

    @Builder
    public TransferScheduleTransaction(
            final AccountAddress sender,
            final AccountAddress to,
            final Schedule[] schedule,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(to)) {
            throw TransactionCreationException.from(new IllegalArgumentException("To cannot be null"));
        }
        if (Objects.isNull(schedule)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Schedule cannot be null"));
        }
        if (schedule.length > 255) {
            throw TransactionCreationException.from(new IllegalArgumentException("Schedule size can be maximum 255"));
        }
        this.to = to;
        this.schedule = schedule;
    }

    /**
     * This function returns the block item associated with this block.
     */
    @Override
    public BlockItem getBlockItem() {
        return TransferSchedule.createNew(getTo(), getSchedule())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
