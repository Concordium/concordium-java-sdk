package com.concordium.sdk.transactions;

import com.concordium.grpc.v2.BlockHash;
import com.concordium.grpc.v2.TransactionHash;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.util.Optional;

/**
 * A common hash (SHA256) used on the chain.
 */
@EqualsAndHashCode
@JsonSerialize(using = Hash.HashSerializer.class)
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

    public static Optional<ImmutableList<Hash>> fromJsonArray(String jsonValue) {
        try {
            val arr = JsonMapper.INSTANCE.readValue(jsonValue, Hash[].class);
            return Optional.ofNullable(arr).map(ImmutableList::copyOf);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Hash Array JSON", e);
        }
    }

    @Override
    public String toString() {
        return this.asHex();
    }

    public static class HashSerializer extends JsonSerializer<Hash> {
        @Override
        public void serialize(Hash value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.asHex());
        }
    }
}
