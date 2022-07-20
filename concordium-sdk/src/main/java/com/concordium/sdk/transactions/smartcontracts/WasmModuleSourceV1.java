package com.concordium.sdk.transactions.smartcontracts;

public class WasmModuleSourceV1 extends WasmModuleSource {

    private static final long MAX_SIZE = 8 * 65536; // 512 kb

    protected WasmModuleSourceV1(byte[] rawSource) {
        super(rawSource);
    }

    public static WasmModuleSourceV1 from(byte[] bytes) {
        if(bytes.length >= MAX_SIZE) {
            throw new IllegalArgumentException("Wasm V1 modules may not exceed " + MAX_SIZE + " bytes.");
        }
        return new WasmModuleSourceV1(bytes);
    }

    @Override
    public WasmModuleVersion getVersion() {
        return WasmModuleVersion.V1;
    }
}
