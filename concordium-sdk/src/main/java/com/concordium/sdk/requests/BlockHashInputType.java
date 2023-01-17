package com.concordium.sdk.requests;

/**
 * Type of {@link BlockHashInput}
 */
public enum BlockHashInputType {
    /**
     * Best Block
     */
    BEST,

    /**
     * Lst Finalized Block
     */
    LAST_FINAL,

    /**
     * Input / Given Block
     */
    GIVEN
}
