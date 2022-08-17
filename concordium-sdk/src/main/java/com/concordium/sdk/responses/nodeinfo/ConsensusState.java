package com.concordium.sdk.responses.nodeinfo;

/**
 * Consensus State of the Node.
 */
public enum ConsensusState {
    /**
     * The node is not running consensus.
     */
    NotRunning,

    /**
     * The node is running consensus but it is not baking.
     */
    Passive,

    /**
     * The node is a baker.
     */
    Active
}
