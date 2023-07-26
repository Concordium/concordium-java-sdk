package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.concordium.sdk.Constants.UTC_ZONE;

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
     * Get the UTC timestamp
     * @return the UTC timestamp
     */
    public ZonedDateTime getDate() {
        return Instant.EPOCH.plusMillis(millis).atZone(UTC_ZONE);
    }

    @Override
    public String toString() {
        return getDate().toString();
    }
}
