package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * Transfer an amount with schedule and memo.
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public final class TransferScheduleWithMemo extends Payload {

    private final AccountAddress to;
    private final Schedule[] amount;
    private final UInt64 maxEnergyCost;
    private final Memo memo;

    public TransferScheduleWithMemo(AccountAddress to, Schedule[] amount, Memo memo, UInt64 maxEnergyCost) {
        this.to = to;
        this.amount = amount;
        this.memo = memo;
        this.maxEnergyCost = maxEnergyCost;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER_WITH_SCHEDULE_AND_MEMO;
    }

    public byte getTransactionType() {
        return TransactionType.TRANSFER_WITH_SCHEDULE_AND_MEMO.getValue();
    }

    @Override
    byte[] getBytes() {
        val schedule_len = amount.length;
        val schedule_buffer_size = UInt64.BYTES * schedule_len * 2;
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + TransactionType.BYTES + AccountAddress.BYTES + memo.getLength() + schedule_buffer_size);

        buffer.put(this.getTransactionType());
        buffer.put(to.getBytes());
        buffer.put(memo.getBytes());

        buffer.put((byte)schedule_len);
        for (int i = 0; i < schedule_len; i++) {
            val schedule_buffer = amount[i].getBytes();
            buffer.put(schedule_buffer);
        }

        return buffer.array();
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    static TransferScheduleWithMemo createNew(AccountAddress to, Schedule[] amount, Memo memo, UInt64 maxEnergyCost) {
        return new TransferScheduleWithMemo(to, amount, memo, maxEnergyCost);
    }
}