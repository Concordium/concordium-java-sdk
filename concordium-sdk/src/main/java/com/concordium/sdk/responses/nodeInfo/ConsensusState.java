package com.concordium.sdk.responses.nodeInfo;

/**
 * Consensus State of the Node.
 */
public enum ConsensusState {
    /**
     * Node's not running Consensus.
     */
    NotRunning,

    /**
     * Node is running Consensus but is not Baking.
     */
    Passive,

    /**
     * Node is a Baker.
     */
    Active
}
