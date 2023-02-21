package com.concordium.sdk.responses;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * Identification of a block. See {@link ClientV2#getBlocks()} & {@link ClientV2#getFinalizedBlocks()} ()}.
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Builder
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
