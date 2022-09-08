package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;

import lombok.val;
import java.nio.ByteBuffer;

public class ContractAddress {
    public final UInt64 index;
    public final UInt64 subindex;

    public ContractAddress(UInt64 index, UInt64 subindex) {
        this.index = index;
        this.subindex = subindex;
    }

    public static ContractAddress from(int index, int subindex) {
        return new ContractAddress(UInt64.from(index), UInt64.from(subindex));
    }

    public byte[] getBytes() {
        val index_bytes = index.getBytes();
        val subindex_bytes = subindex.getBytes();
        val buffer = ByteBuffer.allocate(UInt64.BYTES + UInt64.BYTES );
        buffer.put(index_bytes);
        buffer.put(subindex_bytes);
        return buffer.array();
    }
}
