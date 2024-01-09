package com.concordium.sdk.crypto.pointchevalsanders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.util.Arrays;

/**
 * Public Key for Pointcheval-Sanders Signing scheme.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@JsonSerialize(using = PSPublicKey.JacksonSerializer.class)
public class PSPublicKey {

    /**
     * Bytes for the public key.
     */
    private final byte[] bytes;

    @JsonCreator
    public static PSPublicKey from(String hexKey) {
        try {
            return new PSPublicKey(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create PSPublicKey", e);
        }
    }

    public static PSPublicKey from(final byte[] bytes) {
        return new PSPublicKey(Arrays.copyOf(bytes, bytes.length));
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    /**
     * A custom Jackson serializer is provided that makes the PSPublicKey JSON serialization
     * compatible with the JSON format expected by the Rust libraries.
     */
    static class JacksonSerializer extends StdSerializer<PSPublicKey> {
        public JacksonSerializer() {
            this(null);
        }

        public JacksonSerializer(Class<PSPublicKey> t) {
            super(t);
        }

        @Override
        public void serialize(
                PSPublicKey publicKey, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {

            jgen.writeString(Hex.encodeHexString(publicKey.getBytes()));
        }
    }
}
