package com.concordium.sdk.responses.nodeinfov2;

/**
 * Represents committee state of a node configured with baker keys
 */
public enum BakingCommitteeStatus {

    /**
     * The node is started with baker keys however it is currently not in the baking committee
     * The node is not baking
     */
    NOT_IN_COMMITTEE,

    /**
     * The account is registered as a baker but not in the current Epoch
     * The node is not baking
     */
    ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE,

    /**
     * The node has configured invalid baker keys i.e., the configured baker keys do not match the current keys on the baker account
     * The node is not baking
     */
    ADDED_BUT_WRONG_KEYS,

    /**
     * The node is configured wit baker keys and is active in the current baking committee
     */
    ACTIVE_BAKER,

    /**
     * The node is configured with baker keys and is active in the current finalizer committee (and also baking committee)
     */
    ACTIVE_FINALIZER
}
