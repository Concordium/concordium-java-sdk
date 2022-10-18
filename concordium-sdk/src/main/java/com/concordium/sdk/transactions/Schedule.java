package com.concordium.sdk.transactions;

import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * The release schedule.
 */
public class Schedule {

    private final Timestamp time;
    private final CCDAmount amount;

    public Schedule(Timestamp time, CCDAmount amount) {
        this.time = time;
        this.amount = amount;
    }


    public static Schedule from(long time, int amount) {
        return new Schedule(Timestamp.newMillis(time), CCDAmount.fromMicro(amount));
    }

    public byte[] getBytes() {
        val time_in_milli = time.getMillis();
        val buffer = ByteBuffer.allocate(UInt64.BYTES + UInt64.BYTES);
        buffer.put(UInt64.from(time_in_milli).getBytes());
        buffer.put(amount.getBytes());
        return buffer.array();
    }
}
