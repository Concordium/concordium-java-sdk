package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
public class UInt8 {

    public static final int BYTES = Byte.BYTES;
    final int value;

    private UInt8(int value) {
        this.value = value;
    }

    public byte[] getBytes() {
        val bytes = new byte[1];
        bytes[0] = (byte) (value & 0xff);
        return bytes;
    }

    public static UInt8 from(String value) {
        return UInt8.from(Integer.parseUnsignedInt(value));
    }

    public static UInt8 from(int value) {
        if (value < 0) {
            throw new NumberFormatException("Value of UInt8 cannot be negative");
        }
        if (value > 255) {
            throw new NumberFormatException("Value of UInt8 cannot exceed 2^8");
        }
        return new UInt8(value);
    }

    public static UInt8 from(byte[] valueBytes) {
        val buffer = ByteBuffer.allocate(Byte.BYTES);
        buffer.put(valueBytes);
        return new UInt8(buffer.get(0) & 0xff);
    }

    public static UInt8 fromBytes(ByteBuffer source) {
        byte[] valueBytes = new byte[1];
        source.get(valueBytes);
        return UInt8.from(valueBytes);
    }

    public String toString() {return String.valueOf(value);}
}
