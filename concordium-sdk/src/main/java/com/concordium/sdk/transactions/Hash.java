package com.concordium.sdk.transactions;

import com.concordium.grpc.v2.BlockHash;
import com.concordium.grpc.v2.TransactionHash;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

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

}
