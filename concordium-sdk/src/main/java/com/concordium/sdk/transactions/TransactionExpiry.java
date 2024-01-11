package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;

import java.nio.ByteBuffer;

/**
 * A Unix timestamp
 */
public class TransactionExpiry {
    private final UInt64 expiry;

    TransactionExpiry(UInt64 expiry) {
        this.expiry = expiry;
    }

    public byte[] getBytes() {
        return this.expiry.getBytes();
    }

    public static TransactionExpiry fromBytes(ByteBuffer source) {
        return new TransactionExpiry(UInt64.fromBytes(source));
    }

    public static TransactionExpiry fromLong(long source) {
        return new TransactionExpiry(UInt64.from(source));
    }
}
