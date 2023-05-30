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
     * Last Finalized Block
     */
    LAST_FINAL,

    /**
     * Input / Given Block
     */
    GIVEN,

    /**
     * Input / Relative block height
     */
    RELATIVE,

    /**
     * Input / Absolute block height
     */
    ABSOLUTE
}
