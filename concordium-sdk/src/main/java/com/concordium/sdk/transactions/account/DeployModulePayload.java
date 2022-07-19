package com.concordium.sdk.transactions.account;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class DeployModulePayload implements IAccountTransactionPayload2{

    @Getter
    private final TransactionType type = TransactionType.DEPLOY_MODULE;

    @Getter
    private final WasmModuleVersion version;

    @Getter
    private final WasmModuleSource source;

    @Override
    public byte[] serialize() {
        val buffer = ByteBuffer.allocate(
                WasmModuleVersion.SIZE
                        + UInt32.BYTES
                        + this.source.getLength()
        );
        buffer.put(this.version.serialize());
        buffer.putInt(this.source.getLength());
        buffer.put(this.source.serialize());
        return buffer.array();
    }
}
