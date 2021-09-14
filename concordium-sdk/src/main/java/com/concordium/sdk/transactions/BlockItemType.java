package com.concordium.sdk.transactions;

enum BlockItemType {
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

    static final int BYTES = 1;
}
