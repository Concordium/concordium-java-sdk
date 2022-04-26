package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public enum BlockItemType {
    ACCOUNT_TRANSACTION((byte)0),
    CREDENTIAL_DEPLOYMENT((byte)1),
    UPDATE_INSTRUCTION((byte)2);

    private final byte value;

    BlockItemType(byte value) {
        this.value = value;
    }

    byte getByte() {
        return this.value;
    }

    public static BlockItemType fromBytes(ByteBuffer source) {
        byte tag = source.get();
        switch (tag) {
            case 0: return ACCOUNT_TRANSACTION;
            case 1: return CREDENTIAL_DEPLOYMENT;
            case 2: return UPDATE_INSTRUCTION;
            default: throw new IllegalArgumentException();
        }
    }

    static final int BYTES = 1;
}
