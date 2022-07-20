package com.concordium.sdk.transactions.smartcontracts;

public class WasmModuleSourceV0 extends WasmModuleSource {

    private static final long MAX_SIZE = 65536; // 64 kb

    WasmModuleSourceV0(byte[] bytes) {
        super(bytes);
    }

    @Override
    public WasmModuleVersion getVersion() {
        return WasmModuleVersion.V0;
    }

    public static WasmModuleSourceV0 from(byte[] bytes) {
        if(bytes.length >= MAX_SIZE) {
            throw new IllegalArgumentException("Wasm V0 modules may not exceed " + MAX_SIZE + " bytes.");
        }
        return new WasmModuleSourceV0(bytes);
    }
}
