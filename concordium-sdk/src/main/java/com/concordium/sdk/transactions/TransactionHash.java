package com.concordium.sdk.transactions;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

@Setter
public final class TransactionHash {
    private final byte[] hashBytes;

    private TransactionHash(byte[] hash) {
        this.hashBytes = hash;
    }

    public String getHash() {
        return Hex.encodeHexString(this.hashBytes);
    }

    @SneakyThrows
    public static TransactionHash from(String hexHash) {
        val bytes = Hex.decodeHex(hexHash);
        return new TransactionHash(bytes);
    }

    public static TransactionHash from(byte[] hash) {
        return new TransactionHash(hash);
    }
}
