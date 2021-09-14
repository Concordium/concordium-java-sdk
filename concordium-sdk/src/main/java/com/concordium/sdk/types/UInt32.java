package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
public class UInt32 {
    public static final int BYTES = Integer.BYTES;
    private final int value;

    private UInt32(int value) {
        this.value = value;
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(this.value);
        return buffer.array();
    }

    public static UInt32 from(String value) {
        return new UInt32(Integer.parseUnsignedInt(value));
    }

    public static UInt32 from(int value)  {
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

}
