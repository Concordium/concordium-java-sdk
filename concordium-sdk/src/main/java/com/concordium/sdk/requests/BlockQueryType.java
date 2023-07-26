package com.concordium.sdk.requests;

/**
 * Type of {@link BlockQuery}
 */
public enum BlockQueryType {
    /**
     * Best Block
     */
    BEST,

    /**
     * Lst Finalized Block
     */
    LAST_FINAL,

    /**
     * Query a block by its hash
     */
    GIVEN,
    /**
     * Query a block at a certain height.
     */
    HEIGHT
}
