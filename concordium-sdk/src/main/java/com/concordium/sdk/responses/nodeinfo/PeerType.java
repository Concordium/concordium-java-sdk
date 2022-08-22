package com.concordium.sdk.responses.nodeinfo;

/**
 * Represents the node type.
 */
public enum PeerType {
    /**
     * Node is a bootstrapper and it does not participate in consensus.
     */
    BOOTSTRAPPER,

    /**
     * Node of type Node.
     */
    NODE,
}
