package com.concordium.sdk.types;

import com.concordium.sdk.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * A Unix like timestamp with 1/1-1970 as the offset with millisecond precision.
 */
@EqualsAndHashCode
public class Timestamp {

    @Getter
    private final long millis;

    @JsonCreator
    Timestamp(long millis) {
        this.millis = millis;
    }

    @JsonCreator
    public Timestamp(OffsetDateTime dateTime) {
        this.millis = dateTime.toInstant().toEpochMilli();
    }

    /**
     * Create a new {@link Timestamp} from milliseconds.
     *
     * @param millis the amount of milliseconds that have passed since 1/1-1970.
     * @return a new timestamp
     */
    public static Timestamp newMillis(long millis) {
        return new Timestamp(millis);
    }

    /**
     * Create a new {@link Timestamp} from seconds i.e. a Unix timestamp.
     *
     * @param seconds the amount of seconds that have passed since 1/1-1970.
     * @return a new timestamp
     */
    public static Timestamp newSeconds(long seconds) {
        return new Timestamp(seconds * 1000);
    }

    /**
     * Construct a timestamp from a {@link Instant}
     * @param instant the time
     * @return the corresponding timestamp.
     */
    public static Timestamp from(Instant instant) {
        return Timestamp.newMillis(instant.toEpochMilli());
    }

    /**
     * Get the UTC timestamp as a {@link ZonedDateTime}
     * @return the time of the timestamp.
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.from(Instant.EPOCH.plusMillis(millis).atZone(Constants.UTC_ZONE));
    }

    public static Timestamp from(com.concordium.grpc.v2.Timestamp ts) {
        return Timestamp.newMillis(ts.getValue());
    }

    @Override
    public String toString() {
        return getZonedDateTime().toString();
    }
}
