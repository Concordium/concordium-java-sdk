package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

@EqualsAndHashCode
@Getter
public class UInt8 {

    private final int value;

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

    public static UInt8 from(byte b) {
        return new UInt8(b & 0xff);
    }

    public String toString() {return String.valueOf(value);}
}
