package com.concordium.sdk.responses.poolstatus;

public enum PoolType {
    /**
     * A baker and delegators that collectively pool their stake to participate
     * in the consensus protocol and earn rewards. The baker runs a baker node on behalf of the baker pool to bake
     * (and possibly finalize) blocks using the collective stake of the pool to determine its lottery power.
     */
    BAKER_POOL,
}
