package com.concordium.sdk.transactions;

import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;

import java.util.Date;

/**
 * Unix timestamp i.e. seconds since unix epoch.
 */
public final class Expiry {
    private final Timestamp timestampInMillis;

    private Expiry(Timestamp value) {
        this.timestampInMillis = value;
    }

    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;

    /**
     * Create a new `Expiry` with current offset added the amount of minutes.
     *
     * @param minutes minutes to add.
     *                The amount of minutes must be positive.
     * @return The Expiry with the added minutes.
     */
    public Expiry addMinutes(int minutes) {
        if (minutes < 1) {
            throw new IllegalArgumentException("Minutes must be positive.");
        }
        return Expiry.from(Timestamp.newMillis(this.timestampInMillis.getMillis() + ((long) minutes * MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE)));
    }

    /**
     * Create a new `Expiry` with current offset added the amount of seconds.
     *
     * @param seconds seconds to add.
     *                The amount of seconds provided must be positive.
     * @return The Expiry with the added minutes.
     */
    public Expiry addSeconds(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be positive.");
        }
        return Expiry.from(Timestamp.newMillis(this.timestampInMillis.getMillis() + (long) seconds * MILLISECONDS_PER_SECOND));
    }

    /**
     * Create an `Expiry` from a raw unix timestamp.
     *
     * @param value the raw unix timestamp i.e., seconds since unix epoch.
     * @return the Expiry
     */
    public static Expiry from(long value) {
        if (value == 0) {
            throw new IllegalArgumentException("Expiry cannot be zero");
        }
        return new Expiry(Timestamp.newSeconds(value));
    }

    /**
     * Create a new `Expiry` with an offset of the current time.
     *
     * @return the Expiry
     */
    public static Expiry createNew() {
        return Expiry.from(Timestamp.newMillis(System.currentTimeMillis()));
    }

    /**
     * Create an `Expiry` from a {@link Date}
     *
     * @param date the date
     * @return the expiry
     */
    public static Expiry from(Date date) {
        return new Expiry(Timestamp.newMillis(date.getTime()));
    }

    /**
     * Create an `Expiry` from a {@link Timestamp}
     *
     * @param timestamp the timestamp
     * @return the expiry
     */
    public static Expiry from(Timestamp timestamp) {
        return new Expiry(timestamp);
    }

    UInt64 getValue() {
        return UInt64.from(timestampInMillis.getMillis() / 1000);
    }

    @Override
    public String toString() {
        return timestampInMillis.toString();
    }

}
