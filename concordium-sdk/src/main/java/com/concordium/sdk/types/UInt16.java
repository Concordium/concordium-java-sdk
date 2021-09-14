package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode
public class UInt16 {
    private final int value;

    private UInt16(int value) {
        this.value = value;
    }

    //Big endian
    public byte[] getBytes() {
        val bytes = new byte[2];
        bytes[0] = (byte) ((value >> 8) & 0xff);
        bytes[1] = (byte) (value & 0xff);
        return bytes;
    }

    public static UInt16 from(String value) {
        return UInt16.from(Integer.parseUnsignedInt(value));
    }

    public static UInt16 from(int value) {
        if (value < 0) {
            throw new NumberFormatException("Value of UInt16 cannot be negative");
        }
        if (value > 65535) {
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

}
