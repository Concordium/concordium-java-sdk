package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.ByteBuffer;

/**
 * A Unix timestamp
 */
public class TransactionExpiry {
    @JsonProperty
    private final UInt64 expiry;

    TransactionExpiry(UInt64 expiry) {
        this.expiry = expiry;
    }

    @JsonIgnore
    public byte[] getBytes() {
        return this.expiry.getBytes();
    }

    @JsonIgnore
    public UInt64 getExpiry() {
        return expiry;
    }

    public static TransactionExpiry fromBytes(ByteBuffer source) {
        return new TransactionExpiry(UInt64.fromBytes(source));
    }

    /**
     * Construct a {@link TransactionExpiry} from a long. Note that as the value
     * goes into a {@link UInt64} that it will be interpreted as an unsigned UInt64.
     */
    public static TransactionExpiry fromLong(long source) {
        return new TransactionExpiry(UInt64.from(source));
    }
}
