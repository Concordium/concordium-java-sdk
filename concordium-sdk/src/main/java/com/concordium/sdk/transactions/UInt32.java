package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
class UInt32 {
    static final int BYTES = Integer.BYTES;
    final int value;

    private UInt32(int value) {
        this.value = value;
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(this.value);
        return buffer.array();
    }

    static UInt32 from(String value) {
        return new UInt32(Integer.parseUnsignedInt(value));
    }

    static UInt32 from(int value)  {
        if (value < 0) {
            throw new NumberFormatException("Value of UInt32 can not be negative");
        }
        return new UInt32(value);
    }

    static UInt32 from(byte[] valueBytes) {
        val buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(valueBytes);
        buffer.flip();
        return new UInt32(buffer.getInt());
    }

}
