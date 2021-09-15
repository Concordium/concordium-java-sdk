package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
@ToString
final class UInt64 {
    static final int BYTES = Long.BYTES;
    private final long value;

    private UInt64(long value) {
        this.value = value;
    }

     byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(this.value);
        return buffer.array();
    }

    static UInt64 from(String value) {
        return new UInt64(Long.parseUnsignedLong(value));
    }

    static UInt64 from(long value) {
        if (value < 0) {
            throw new NumberFormatException("Value of UInt64 can not be negative");
        }
        return new UInt64(value);
    }

    static UInt64 from(byte[] valueBytes) {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(valueBytes);
        buffer.flip();
        return new UInt64(buffer.getLong());
    }
}
