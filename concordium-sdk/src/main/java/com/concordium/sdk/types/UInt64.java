package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
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
    static class UInt64Serializer extends JsonSerializer<UInt64> {

        @Override
        public void serialize(UInt64 uint,
                              JsonGenerator generator,
                              SerializerProvider provider) throws IOException {

            if (generator instanceof CBORGenerator) {
                // Write as unsigned 8-byte integer.
                val cborGen = (CBORGenerator) generator;
                cborGen.writeRaw((byte) 0x1B);
                cborGen.writeBytes(uint.getBytes(), 0, Long.BYTES);
                return;
            }

            generator.writeRawValue(Long.toUnsignedString(uint.getValue()));
        }
    }
}
