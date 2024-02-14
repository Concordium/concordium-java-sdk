package com.concordium.sdk.types;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
@JsonSerialize(using = UInt32.UInt32Serializer.class)
public final class UInt32 implements Comparable<UInt32> {
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

    /**
     * Construct a 32 bits wide integer that is interpreted as unsigned.
     *
     * @param value the integer treated as unsigned.
     * @return the resulting {@link UInt32}
     */
    public static UInt32 from(int value) {
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
        return Integer.toUnsignedString(value);
    }

    @Override
    public int compareTo(UInt32 other) {
        return Integer.compareUnsigned(this.value, other.value);
    }

    /**
     * A custom Jackson serializer is provided that ensures that the unsigned value
     * is the one used when serializing to JSON.
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
            jgen.writeRawValue(Integer.toUnsignedString(uint.getValue()));
        }
    }
}
