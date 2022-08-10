package com.concordium.sdk.responses.nodeInfo;

/**
 * Represents Baker nodes status.
 */
public enum NodeActiveStateType {
    /**
     * Node is not part of Baking Committee.
     */
    NotInCommittee,

    /**
     * Node is part of Baking Committee but not active.
     */
    AddedButNotActiveInCommittee,

    /**
     * Node is part of the Baking Committee but has the wrong keys.
     */
    AddedButWrongKeys,

    /**
     * Node is active in the Baking Committee.
     */
    ActiveInCommittee,

    /**
     * Unrecognized State.
     */
    Unrecognized,
}
