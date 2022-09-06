package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.types.UInt32;
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

    byte[] getBytes() {
        return UInt32.from(this.tag).getBytes();
    }

}
