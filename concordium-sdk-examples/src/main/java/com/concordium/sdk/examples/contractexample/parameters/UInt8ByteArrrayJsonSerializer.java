package com.concordium.sdk.examples.contractexample.parameters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializes a byte[] interpreted as unsigned bytes.
 */
public class UInt8ByteArrrayJsonSerializer extends JsonSerializer<byte[]> {
    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (byte b : value) {
            int unsigned = b & 0xff; // The unsigned value of the byte
            gen.writeNumber(unsigned);
        }
        gen.writeEndArray();
    }
}
