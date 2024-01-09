package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@EqualsAndHashCode
@Getter
@JsonSerialize(using = UInt32.UInt32Serializer.class)
public final class UInt32 {
    public static final int BYTES = Integer.BYTES;
    final int value;

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

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * A custom Jackson serializer is provided that makes the UInt32 JSON serialization
     * compatible with the JSON format expected by the Rust libraries.
     */
    static class UInt32Serializer extends StdSerializer<UInt32> {

        public UInt32Serializer() {
            this(null);
        }

        public UInt32Serializer(Class<UInt32> t) {
            super(t);
        }

        @Override
        public void serialize(
                UInt32 uint, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {

            jgen.writeNumber(uint.getValue());
        }
    }

}
