package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableList;
import lombok.*;

/**
 * A list of block hashes at a particular branch height.
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class BranchBlocks {

    /**
     * Block hashes at a particular branch height.
     */
    @Singular("blockAtBranchHeight")
    private final ImmutableList<Hash> blocksAtBranchHeight;
}
