package com.concordium.sdk.transactions;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Optional;

/**
 * A common hash (SHA256) used on the chain.
 */
@EqualsAndHashCode
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

    public String asHex() {
        return Hex.encodeHexString(this.bytes);
    }

    public static Hash from(String hexHash) {
        return new Hash(hexHash);
    }

    public static Hash from(byte[] hash) {
        return new Hash(hash);
    }

    public static Optional<ImmutableList<Hash>> fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val arr = JsonMapper.INSTANCE.readValue(res.getValue(), Hash[].class);
            return Optional.ofNullable(arr).map(ImmutableList::copyOf);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Hash Array JSON", e);
        }
    }

    @Override
    public String toString() {
        return this.asHex();
    }

}
