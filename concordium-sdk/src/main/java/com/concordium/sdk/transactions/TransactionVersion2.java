package com.concordium.sdk.transactions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TransactionVersion2 {
    private final int version = 1;

    public byte[] serialize() {
        return new byte[]{(byte) this.version};
    }
}