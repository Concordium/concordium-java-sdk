package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.types.UInt32;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * There exists two versions of wasm versions on the chain; V0 and V1.
 * V1 contracts were introduced as part of {@link ProtocolVersion#V4}.
 * V1 contracts has several benefits over the original V0 contracts.
 * V1 contracts allows for larger state and module size (i.e. the actual code deployed on chain)
 * and they also support _synchronized_ calls between contracts.
 */
public enum WasmModuleVersion {

    /**
     * Wasm Module version 0.
     */
    V0((byte) 0),

    /**
     * Wasm Module Version 1. V1 contracts were added as part of protocol 4.
     */
    V1((byte) 1);

    static final int BYTES = UInt32.BYTES;

    @Getter
    private final byte value;

    WasmModuleVersion(byte value) {
        this.value = value;
    }

    public static WasmModuleVersion from(ByteBuffer bytes) {
        val intValue = UInt32.fromBytes(bytes).getValue();

        return from(intValue);
    }

    private static WasmModuleVersion from(int intValue) {
        switch (intValue) {
            case 0:
                return WasmModuleVersion.V0;
            case 1:
                return WasmModuleVersion.V1;
            default:
                throw new IllegalArgumentException("Could not create WasmModuleVersion from input bytes");
        }
    }

    byte[] getBytes() {
        return UInt32.from(this.value).getBytes();
    }

    @Override
    public String toString() {
        switch (this) {
            case V0:
                return "V0";
            case V1:
                return "V1";
        }
        throw new IllegalStateException("Unexpected version");
    }
}
