package com.concordium.sdk.transactions;

import io.grpc.netty.shaded.io.netty.util.internal.UnstableApi;

import java.util.Arrays;

public class UpdateInstructionTransactionPayload {
    private final byte[] bytes;

    UpdateInstructionTransactionPayload(final byte[] bytes) {
        this.bytes = bytes;
    }

    @UnstableApi
    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    public static UpdateInstructionTransactionPayload from(final byte[] bytes) {
        return new UpdateInstructionTransactionPayload(Arrays.copyOf(bytes, bytes.length));
    }
}
