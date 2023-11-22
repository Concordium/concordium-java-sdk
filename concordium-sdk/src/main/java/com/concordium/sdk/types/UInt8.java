package com.concordium.sdk.types;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.io.IOException;

/**
 * An unsigned byte
 */
@EqualsAndHashCode
@Getter
@JsonSerialize(using = UInt8.UInt8Serializer.class)
public class UInt8 {

    private final int value;

    private UInt8(int value) {
        this.value = value;
    }

    public byte[] getBytes() {
        val bytes = new byte[1];
        bytes[0] = (byte) (value);
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

    @Override
    public String toString() {return String.valueOf(value);}

    public static class UInt8Serializer extends JsonSerializer<UInt8> {
        @Override
        public void serialize(UInt8 uInt8, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeNumber(uInt8.getValue());
        }
    }
}
