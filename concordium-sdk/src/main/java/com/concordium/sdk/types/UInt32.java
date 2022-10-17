package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class UInt32 {
    public static final int BYTES = Integer.BYTES;
    private final int value;

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(this.value);
        return buffer.array();
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonCreator
    public static UInt32 from(String value) {
        return new UInt32(Integer.parseUnsignedInt(value));
    }

    public static UInt32 from(int value) {
        if (value < 0) {
            throw new NumberFormatException("Value of UInt32 can not be negative");
        }
        return new UInt32(value);
    }

    public static UInt32 from(byte[] valueBytes) {
        val buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(valueBytes);
        buffer.flip();
        return new UInt32(buffer.getInt());
    }

    public static UInt32 fromBytes(ByteBuffer source) {
        byte[] valueBytes = new byte[4];
        source.get(valueBytes);
        return UInt32.from(valueBytes);
    }
}
