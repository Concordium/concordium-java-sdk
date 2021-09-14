package com.concordium.sdk.transactions;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

public final class BlockHash {
    private final byte[] hashBytes;

    private BlockHash(byte[] hash) {
        this.hashBytes = hash;
    }

    public String getHash() {
        return Hex.encodeHexString(this.hashBytes);
    }

    @SneakyThrows
    public static BlockHash from(String hexHash) {
        val bytes = Hex.decodeHex(hexHash);
        return new BlockHash(bytes);
    }

    public static BlockHash from(byte[] hash) {
        return new BlockHash(hash);
    }
}
