package com.concordium.sdk.transactions;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

public class BlockItemHash {

    private final byte[] hashBytes;

    private BlockItemHash(byte[] hash) {
        this.hashBytes = hash;
    }

    public String getHash() {
        return Hex.encodeHexString(this.hashBytes);
    }

    @SneakyThrows
    public static BlockItemHash from(String hexHash) {
        val bytes = Hex.decodeHex(hexHash);
        return new BlockItemHash(bytes);
    }

    public static BlockItemHash from(byte[] hash) {
        return new BlockItemHash(hash);
    }
}
