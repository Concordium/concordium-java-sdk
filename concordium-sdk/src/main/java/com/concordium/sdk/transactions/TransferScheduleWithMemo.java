package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.*;

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
     * The account address of the recipient.
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

    @Builder
    public TransferScheduleWithMemo(AccountAddress to,
                                    Schedule[] amount,
                                    Memo memo) {
        super(TransactionType.TRANSFER_WITH_SCHEDULE_AND_MEMO);
        this.to = to;
        this.amount = amount;
        this.memo = memo;
    }

    @Override
    protected byte[] getPayloadBytes() {
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
