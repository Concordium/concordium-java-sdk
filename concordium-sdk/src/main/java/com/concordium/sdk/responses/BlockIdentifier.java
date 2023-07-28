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
 * Identification of a block. See {@link ClientV2#getBlocks(int)} ()} & {@link ClientV2#getFinalizedBlocks(int)} ()} ()}.
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@SuperBuilder
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
