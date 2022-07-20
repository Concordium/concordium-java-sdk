package com.concordium.sdk.transactions.smartcontracts;

import lombok.Getter;

public enum WasmModuleVersion {
    V0((byte) 0),
    V1((byte) 1);

    @Getter
    private final byte tag;

    WasmModuleVersion(byte tag) {
        this.tag = tag;
    }

    public byte getValue() {
        return this.tag;
    }
}
