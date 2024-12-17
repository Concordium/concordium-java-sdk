package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import com.google.common.collect.ImmutableList;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class BlockTableSummary {

    /**
     * The number of blocks in the dead block cache.
     */
    private final UInt64 deadBlockCacheSize;

    /**
     * The blocks that are currently live (not dead and not finalized).
     */
    @Singular
    private final ImmutableList<Hash> liveBlocks;
}
