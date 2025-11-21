package com.concordium.sdk.transactions;

import com.concordium.grpc.v2.BlockHash;
import com.concordium.grpc.v2.TransactionHash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

/**
 * A common hash (SHA256) used on the chain.
 */
@EqualsAndHashCode
@JsonSerialize(using = Hash.HashSerializer.class)
@JsonDeserialize(using = Hash.HashDeserializer.class)
public class Hash {
    @Getter
    private final byte[] bytes;

    @JsonCreator
    protected Hash(String encoded) {
        try {
            this.bytes = Hex.decodeHex(encoded);
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create Hash", e);
        }
    }

    protected Hash(byte[] hash) {
        this.bytes = hash;
    }

    public static Hash from(BlockHash blockHash) {
        return Hash.from(blockHash.getValue().toByteArray());
    }

    public static Hash from(TransactionHash hash) {
        return Hash.from(hash.getValue().toByteArray());
    }

    public String asHex() {
        return Hex.encodeHexString(this.bytes);
    }

    public static Hash from(String hexHash) {
        return new Hash(hexHash);
    }

    public static Hash from(byte[] hash) {
        return new Hash(hash);
    }

    @Override
    public String toString() {
        return this.asHex();
    }

    public static class HashSerializer extends StdSerializer<Hash> {

        protected HashSerializer() {
            super(Hash.class);
        }

        @Override
        public void serialize(Hash value,
                              JsonGenerator generator,
                              SerializerProvider serializers) throws IOException {
            if (generator instanceof CBORGenerator) {
                generator.writeObject(value.getBytes());
                return;
            }

            generator.writeString(value.asHex());
        }
    }

    public static class HashDeserializer extends StdDeserializer<Hash>{

        protected HashDeserializer() {
            super(Hash.class);
        }

        @Override
        public Hash deserialize(JsonParser parser,
                                DeserializationContext ctxt) throws IOException {
            if (parser instanceof CBORParser){
                return Hash.from(parser.getBinaryValue());
            }

            return Hash.from(parser.getValueAsString());
        }
    }
}
