package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class EncryptedAmountIndex {
    private final UInt64 index;

    private EncryptedAmountIndex(UInt64 index) {
        this.index = index;
    }

    @JsonCreator
    public static EncryptedAmountIndex from(final long value) {
        return new EncryptedAmountIndex(UInt64.from(value));
    }

    @Override
    public String toString() {
        return this.index.toString();
    }

    @JsonValue
    public long getValue() {
        return this.index.getValue();
    }
}
