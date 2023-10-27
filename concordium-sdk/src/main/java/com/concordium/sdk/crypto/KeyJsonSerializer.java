package com.concordium.sdk.crypto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

public class KeyJsonSerializer extends JsonSerializer<RawKey> {

    @Override
    public void serialize(RawKey value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(Hex.encodeHexString(value.getRawBytes()));
    }
}
