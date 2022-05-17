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
