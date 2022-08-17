package com.concordium.sdk.responses.nodeinfo;

/**
 * Consensus State of the Node.
 */
public enum ConsensusState {
    /**
     * The node is not running consensus.
     */
    NOT_RUNNING,

    /**
     * The node is running consensus but it is not baking.
     */
    PASSIVE,

    /**
     * The node is a baker.
     */
    ACTIVE
}
