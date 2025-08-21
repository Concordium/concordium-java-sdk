package com.concordium.sdk.serializing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.val;

import java.io.IOException;

public class CborMapper {
    public static ObjectMapper INSTANCE = new CBORMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            // Needed to deserialize Optional Fields
            .registerModule(new Jdk8Module());

    /**
     * Writes given data as a value content.
     * Using just writeBytes() for this purpose breaks array/object element counter in the generator.
     * This method uses reflection.
     */
    public static void writeBytesAsValue(CBORGenerator generator,
                                         byte[] data,
                                         int offset,
                                         int len) throws IOException {
        try {
            val verifyValueWrite = CBORGenerator.class.getDeclaredMethod("_verifyValueWrite", String.class);
            verifyValueWrite.setAccessible(true);
            verifyValueWrite.invoke(generator, "write bytes as value");
            verifyValueWrite.setAccessible(false);
        } catch (Exception e) {
            throw new RuntimeException("Can't verify value write with reflection: " + e.getMessage(), e);
        }

        generator.writeBytes(data, offset, len);
    }
}
