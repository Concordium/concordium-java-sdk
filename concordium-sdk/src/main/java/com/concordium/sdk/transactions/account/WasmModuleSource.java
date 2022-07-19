package com.concordium.sdk.transactions.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WasmModuleSource {

    @Getter
    private final byte[] source;

    @Getter
    private final int length;

    public byte[] serialize() {
        return this.source;
    }
}
