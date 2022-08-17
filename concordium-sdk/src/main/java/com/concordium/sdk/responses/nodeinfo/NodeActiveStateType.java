package com.concordium.sdk.responses.nodeinfo;

/**
 * Represents the baking status of a node.
 */
public enum NodeActiveStateType {
    /**
     * The node is not part of the baking committee.
     */
    NOT_IN_COMMITTEE,

    /**
     * The node has baker keys, but it is not currently a baker.
     */
    ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE,

    /**
     * The node is part of the baking committee, but the keys configured for the node
     * does not match the baker account.
     */
    ADDED_BUT_WRONG_KEYS,

    /**
     * Node is active in the baking committee.
     */
    ACTIVE_IN_COMMITTEE,
}
