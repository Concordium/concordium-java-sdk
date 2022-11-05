package com.concordium.sdk.transactions;

import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * The release schedule.
 */
@Getter
@ToString
@EqualsAndHashCode
public class Schedule {
    /**
     * Timestamp of the amount transfer Schedule
     */
    private final Timestamp time;
    /**
     * The amount to be transferred at the given timestamp.
     */
    private final CCDAmount amount;

    Schedule(Timestamp time, CCDAmount amount) {
        this.time = time;
        this.amount = amount;
    }

    /**
     * Create a new Schedule object from a long time and an amount.
     *
     * @param time   The time in milliseconds since the epoch.
     * @param amount The amount of CCD to be sent.
     * @return A new Schedule object with the given time and amount.
     */
    public static Schedule from(long time, int amount) {
        return new Schedule(Timestamp.newMillis(time), CCDAmount.fromMicro(amount));
    }

    /**
     * @return The byte array of {@link Schedule} object.
     */
    public byte[] getBytes() {
        val time_in_milli = time.getMillis();
        val buffer = ByteBuffer.allocate(UInt64.BYTES + UInt64.BYTES);
        buffer.put(UInt64.from(time_in_milli).getBytes());
        buffer.put(amount.getBytes());
        return buffer.array();
    }
}
