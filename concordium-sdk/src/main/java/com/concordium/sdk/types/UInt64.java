package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
@JsonSerialize(using = UInt64.UInt64Serializer.class)
public final class UInt64 implements Comparable<UInt64> {
    public static final int BYTES = Long.BYTES;

    /**
     * The internal value.
     * This should be treated as an unsigned
     */
    private final long value;

    @JsonCreator
    public UInt64(long value) {
        this.value = value;
    }

    @JsonCreator
    public UInt64(BigInteger value) {
        this.value = UInt64.from(value.toString()).getValue();
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(this.value);
        return buffer.array();
    }

    public static UInt64 from(String value) {
        return new UInt64(Long.parseUnsignedLong(value));
    }

    public static UInt64 from(long value) {
        return new UInt64(value);
    }

    public static UInt64 from(byte[] valueBytes) {
        val buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(valueBytes);
        buffer.flip();
        return new UInt64(buffer.getLong());
    }

    public static UInt64 fromBytes(ByteBuffer source) {
        byte[] valueBytes = new byte[8];
        source.get(valueBytes);
        return UInt64.from(valueBytes);
    }

    @Override
    public String toString() {
        return Long.toUnsignedString(value);
    }

    @Override
    public int compareTo(UInt64 other) {
        return Long.compareUnsigned(this.value, other.value);
    }

    /**
     * A custom Jackson serializer is provided that ensures that the unsigned value
     * is the one used when serializing to JSON.
     */
    static class UInt64Serializer extends StdSerializer<UInt64> {

        public UInt64Serializer() {
            this(null);
        }

        public UInt64Serializer(Class<UInt64> t) {
            super(t);
        }

        @Override
        public void serialize(
                UInt64 uint, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeRawValue(Long.toUnsignedString(uint.getValue()));
        }
    }
}
