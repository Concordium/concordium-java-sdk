package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class UInt16 {
    public static final int BYTES = 2;
    public static final int MAX_VALUE = 65535;

    private final int value;

    //Big endian
    public byte[] getBytes() {
        val bytes = new byte[2];
        bytes[0] = (byte) ((value >> 8) & 0xff);
        bytes[1] = (byte) (value & 0xff);
        return bytes;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonCreator
    public static UInt16 from(String value) {
        return UInt16.from(Integer.parseUnsignedInt(value));
    }

    public static UInt16 from(int value) {
        if (value < 0) {
            throw new NumberFormatException("Value of UInt16 cannot be negative");
        }
        if (value > MAX_VALUE) {
            throw new NumberFormatException("Value of UInt16 cannot exceed 2^16");
        }
        return new UInt16(value);
    }

    public static UInt16 from(byte[] valueBytes) {
        val buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.put(valueBytes);
        buffer.flip();
        return new UInt16(buffer.getShort());
    }

    public static UInt16 fromBytes(ByteBuffer source) {
        byte[] valueBytes = new byte[2];
        source.get(valueBytes);
        return UInt16.from(valueBytes);
    }
}
