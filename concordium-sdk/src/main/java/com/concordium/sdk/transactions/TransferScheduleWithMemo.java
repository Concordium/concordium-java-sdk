package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

import static com.concordium.sdk.transactions.TransferSchedule.SCHEDULE_LENGTH_BYTES;

/**
 * Transfer an amount with schedule and memo.
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public final class TransferScheduleWithMemo extends Payload {
    /**
     * The account address of the recepient.
     */
    private final AccountAddress to;
    /**
     * The release schedule. This can be at most 255 elements.
     */
    private final Schedule[] amount;

    /**
     * The memo message associated with the transfer.
     */
    private final Memo memo;

    public TransferScheduleWithMemo(AccountAddress to, Schedule[] amount, Memo memo) {
        this.to = to;
        this.amount = amount;
        this.memo = memo;
    }

    static TransferScheduleWithMemo createNew(AccountAddress to, Schedule[] amount, Memo memo) {
        return new TransferScheduleWithMemo(to, amount, memo);
    }

    /**
     * This function returns the transaction type of this transaction.
     */
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER_WITH_SCHEDULE_AND_MEMO;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        val schedule_len = amount.length;
        val schedule_buffer_size = UInt64.BYTES * schedule_len * 2;
        val buffer = ByteBuffer.allocate(
                AccountAddress.BYTES
                        + memo.getLength()
                        + SCHEDULE_LENGTH_BYTES
                        + schedule_buffer_size);
        buffer.put(to.getBytes());
        buffer.put(memo.getBytes());
        buffer.put((byte) schedule_len);
        for (int i = 0; i < schedule_len; i++) {
            val schedule_buffer = amount[i].getBytes();
            buffer.put(schedule_buffer);
        }

        return buffer.array();
    }
}
