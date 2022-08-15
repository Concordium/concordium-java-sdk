package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
@ToString
public final class UInt64 {
    public static final int BYTES = Long.BYTES;
    private final long value;

    private UInt64(long value) {
        this.value = value;
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(this.value);
        return buffer.array();
    }

    public static UInt64 from(String value) {
        return new UInt64(Long.parseUnsignedLong(value));
    }

    public static UInt64 fromHex(String value) {
        return UInt64.from(Long.parseLong(value, 16));
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
}
