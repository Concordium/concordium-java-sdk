package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.math.BigInteger;
import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
public final class UInt64 {
    public static final int BYTES = Long.BYTES;
    private final long value;

    @JsonCreator
    public UInt64(long value) {
        this.value = value;
    }

    @JsonCreator
    public UInt64(BigInteger value) {
        this.value = UInt64.from(value.toString()).getValue();
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(this.value);
        return buffer.array();
    }

    public static UInt64 from(String value) {
        return new UInt64(Long.parseUnsignedLong(value));
    }

    public static UInt64 from(long value) {
        if (value < 0) {
            throw new NumberFormatException("Value of UInt64 can not be negative");
        }
        return new UInt64(value);
    }

    public static UInt64 from(byte[] valueBytes) {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(valueBytes);
        buffer.flip();
        return new UInt64(buffer.getLong());
    }

    public static UInt64 fromBytes(ByteBuffer source) {
        byte[] valueBytes = new byte[8];
        source.get(valueBytes);
        return UInt64.from(valueBytes);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
