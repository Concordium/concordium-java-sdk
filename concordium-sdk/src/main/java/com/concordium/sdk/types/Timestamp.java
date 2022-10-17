package com.concordium.sdk.types;

import com.concordium.sdk.transactions.schema.ContractSchemaType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * A Unix like timestamp with 1/1-1970 as the offset with millisecond precision.
 */
@EqualsAndHashCode
public class Timestamp implements ContractSchemaType {

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

    public static Timestamp from(ByteBuffer byteBuffer) {
        return new Timestamp(UInt64.fromBytes(byteBuffer).getValue());
    }

    public Date getDate() {
        return new Date(millis);
    }

    @Override
    public String toString() {
        return getDate().toString();
    }

    @Override
    public byte[] getSchemaBytes() {
        return UInt64.from(millis).getBytes();
    }

    @Override
    public JsonNode getSchemaJson() {
        return LongNode.valueOf(millis);
    }
}
