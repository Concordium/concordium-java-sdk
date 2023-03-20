package com.concordium.sdk.transactions;

import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public enum BlockItemType {
    /**
     * Type of Transaction that are paid by the sender account.
     */
    ACCOUNT_TRANSACTION((byte) 0),

    /**
     * Type of transaction which is used to create new on-Chain Account. They are not paid by anyone.
     */
    CREDENTIAL_DEPLOYMENT((byte) 1),

    /**
     * Update transaction for various on chain parameters.
     */
    UPDATE_INSTRUCTION((byte) 2);

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
            case 0:
                return ACCOUNT_TRANSACTION;
            case 1:
                return CREDENTIAL_DEPLOYMENT;
            case 2:
                return UPDATE_INSTRUCTION;
            default:
                throw new IllegalArgumentException();
        }
    }

    static final int BYTES = 1;
}
