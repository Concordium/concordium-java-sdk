package com.concordium.sdk.transactions.account;

import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public enum WasmModuleVersion {
    V0((byte)0),
    V1((byte)1);

    @Getter
    private final byte value;

    WasmModuleVersion(byte value) {
        this.value = value;
    }

    public byte[] serialize() {
        return new byte[] {this.value};
    }

    public static final int SIZE = 1;
}
