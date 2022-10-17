package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
@Jacksonized
@Builder
public class ContractAddress {
    private final UInt64 index;
    private final UInt64 subindex;

    public static ContractAddress from(int index, int subindex) {
        return new ContractAddress(UInt64.from(index), UInt64.from(subindex));
    }

    @JsonCreator
    public static ContractAddress from(UInt64 index, UInt64 subindex) {
        return new ContractAddress(index, subindex);
    }

    public byte[] getBytes() {
        val index_bytes = index.getBytes();
        val subindex_bytes = subindex.getBytes();
        val buffer = ByteBuffer.allocate(UInt64.BYTES + UInt64.BYTES);
        buffer.put(index_bytes);
        buffer.put(subindex_bytes);
        return buffer.array();
    }
}
