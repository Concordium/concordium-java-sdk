package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public class TransferSchedule extends Payload {

    private final AccountAddress to;
    private final Schedule[] amount;
    private final UInt64 maxEnergyCost;

    public TransferSchedule(AccountAddress to, Schedule[] amount, UInt64 maxEnergyCost) {
        this.to = to;
        this.amount = amount;
        this.maxEnergyCost = maxEnergyCost;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER_WITH_SCHEDULE;
    }

    public byte getTransactionType() {
        return TransactionType.TRANSFER_WITH_SCHEDULE.getValue();
    }

    @Override
    byte[] getBytes() {
        val schedule_len = amount.length;
        val schedule_buffer_size = UInt64.BYTES * schedule_len * 2;

        val buffer = ByteBuffer.allocate(TransactionType.BYTES + TransactionType.BYTES + AccountAddress.BYTES + schedule_buffer_size);
        buffer.put(this.getTransactionType());

        buffer.put(to.getBytes());

        buffer.put((byte)schedule_len);
        for (int i = 0; i < schedule_len; i++) {
            val schedule_buffer = amount[i].getBytes();
            buffer.put(schedule_buffer);
        }
        System.out.println(this.getTransactionType());

        return buffer.array();
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    static TransferSchedule createNew(AccountAddress to, Schedule[] amount, UInt64 maxEnergyCost) {
        return new TransferSchedule(to, amount, maxEnergyCost);
    }
}
