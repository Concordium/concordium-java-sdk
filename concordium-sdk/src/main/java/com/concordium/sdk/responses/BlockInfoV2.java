package com.concordium.sdk.responses;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Information about the Block. See {@link ClientV2#getBlocks()} & {@link ClientV2#getFinalizedBlocks()} ()}.
 */
@RequiredArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class BlockInfoV2 {

    /**
     * Hash of the Block.
     */
    private final Hash blockHash;

    /**
     * Absolute Block Height.
     */
    private final UInt64 blockHeight;
}
