package com.concordium.sdk.responses;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Information about the Block. See {@link ClientV2#getBlocks()} & {@link ClientV2#getFinalizedBlocks()} ()}.
 */
@RequiredArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode
@Getter
public class BlockIdentifier {

    /**
     * Hash of the Block.
     */
    private final Hash blockHash;

    /**
     * Absolute Block Height.
     */
    private final UInt64 blockHeight;
}
