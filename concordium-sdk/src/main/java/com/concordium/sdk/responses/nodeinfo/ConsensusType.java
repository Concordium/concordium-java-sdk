package com.concordium.sdk.responses.nodeinfo;

/**
 * The consensus type describes if the node is eligible for participating in consensus.
 * I.e. if the node has been configured with baker credentials or not.
 * This type is only used for parsing and is not directly exposed in the API.
 * Hence {@link NodeInfo#getBakerInfo()} will be present iff. the {@link ConsensusType}
 * is {@link ConsensusType#ACTIVE}.
 */
enum ConsensusType {
    // The node has no baker credentials is thus only an observer of the consensus protocol.
    PASSIVE("Passive"),
    // The node has baker credentials and can thus potentially participate in baking and finalization.
    ACTIVE("Active");

    private final String tag;

    ConsensusType(String tag) {
        this.tag = tag;
    }

    static ConsensusType getConsensusType(String tag) {
        for (ConsensusType value : ConsensusType.values()) {
            if (value.tag.equals(tag)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported ConsensusType: " + tag);
    }

}
