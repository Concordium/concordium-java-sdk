package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class UInt64 {
    public static final int BYTES = Long.BYTES;
    private final long value;

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(this.value);
        return buffer.array();
    }

    @JsonValue
    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    @JsonCreator
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
}
