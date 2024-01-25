package com.concordium.sdk.transactions;

import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Unix timestamp i.e. seconds since unix epoch.
 */
@EqualsAndHashCode
public final class Expiry {
    
    public static final int BYTES = UInt64.BYTES;

    // The expiry in seconds since unix epoch.
    @JsonProperty
    private final UInt64 expiry;

    private Expiry(UInt64 value) {
        this.expiry = value;
    }

    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;

    /**
     * Create a new `Expiry` with current offset added the amount of minutes.
     * @param minutes minutes to add.
     *                The amount of minutes must be strictly positive.
     * @return The Expiry with the added minutes.
     */
    public Expiry addMinutes(int minutes) {
        if (minutes < 1) {
            throw new IllegalArgumentException("Minutes must be positive.");
        }
        return Expiry.from(this.expiry.getValue() + (minutes * SECONDS_PER_MINUTE));
    }

    /**
     * Create a new `Expiry` with current offset added the amount of seconds.
     * @param seconds seconds to add.
     *                The amount of seconds provided must be strictly positive.
     * @return The Expiry with the added minutes.
     */
    public Expiry addSeconds(int seconds) {
        if (seconds < 1) {
            throw new IllegalArgumentException("Seconds must be positive.");
        }
        return Expiry.from(this.expiry.getValue() + seconds);
    }

    /**
     * Create an `Expiry` from a raw unix timestamp in seconds.
     * @param value the raw unix timestamp i.e., seconds since unix epoch.
     * @return the Expiry
     */
    public static Expiry from(long value) {
        if (value == 0) {
            throw new IllegalArgumentException("Expiry cannot be zero");
        }
        return Expiry.from(Timestamp.newSeconds(value));
    }

    /**
     * Create a new `Expiry` with an offset of the current time.
     * @return the Expiry
     */
    public static Expiry createNew() {
        return Expiry.from(Timestamp.newMillis(System.currentTimeMillis()));
    }

    /**
     * Create an `Expiry` from a {@link Date}. Note that there is a loss of precision
     * when using this as a {@link Date} holds milliseconds and the internal
     * value of a {@link Expiry} is in seconds.
     * @param date the date
     * @return the expiry
     */
    public static Expiry from(Date date) {
        return Expiry.from(Timestamp.newMillis(date.getTime()));
    }

    /**
     * Create an `Expiry` from a {@link Timestamp}. Note that there is a loss of precision
     * when using this as a {@link Timestamp} holds milliseconds and the internal
     * value of a {@link Expiry} is in seconds.
     * @param timestamp the timestamp
     * @return the expiry
     */
    public static Expiry from(Timestamp timestamp) {
        return new Expiry(UInt64.from(timestamp.getMillis() / MILLISECONDS_PER_SECOND));
    }

    public UInt64 getValue() {
        return expiry;
    }

    @Override
    public String toString() {
        return expiry.toString();
    }
}
