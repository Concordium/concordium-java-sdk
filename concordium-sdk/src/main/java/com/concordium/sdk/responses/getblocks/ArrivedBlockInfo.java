package com.concordium.sdk.responses.getblocks;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Information about the arrived Block. See {@link ClientV2#getBlocks()}.
 */
@RequiredArgsConstructor
@Builder
@ToString
public class ArrivedBlockInfo {

    /**
     * Hash of the Block.
     */
    private final Hash blockHash;

    /**
     * Absolute Block Height.
     */
    private final UInt64 blockHeight;
}
