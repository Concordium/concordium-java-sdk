package com.concordium.sdk.responses.branch;

import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableList;
import lombok.*;

/**
 * Represents a Tree of Block {@link Hash}es, The branches above the last finalized block.
 * With head represented by {@link Branch#blockHash} and Children represented by {@link Branch#children}.
 */
@ToString
@Getter
@Builder
@EqualsAndHashCode
public class Branch {

    /**
     * Hash of the block at the HEAD of this branch.
     */
    private final Hash blockHash;

    /**
     * Children of the {@link Branch#blockHash}.
     */
    @Singular
    private final ImmutableList<Branch> children;
}
