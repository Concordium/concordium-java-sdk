package com.concordium.sdk.transactions.account;

import lombok.RequiredArgsConstructor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RequiredArgsConstructor
public class WasmModule {
    private final WasmModuleSource source;
    private final WasmModuleVersion version;

    public byte[] serialize() {
        throw new NotImplementedException();
    }
}
