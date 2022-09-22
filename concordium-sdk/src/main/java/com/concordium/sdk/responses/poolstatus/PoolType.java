package com.concordium.sdk.responses.poolstatus;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PoolType {
    /**
     * A baker and delegators that collectively pool their stake to participate
     * in the consensus protocol and earn rewards. The baker runs a baker node on behalf of the baker pool to bake
     * (and possibly finalize) blocks using the collective stake of the pool to determine its lottery power.
     */
    BAKER_POOL,

    /**
     * A form of delegation where a delegator’s stake is effectively distributed among all baker pools.
     * It is not associated with a specific baker.
     * Delegators earn lower rewards when delegating to passive delegation than when delegating to a specific baker pool.
     */
    PASSIVE_DELEGATION;

    @JsonCreator
    public static PoolType from(String value) {
        switch (value) {
            case "BakerPool":
                return PoolType.BAKER_POOL;
            case "PassiveDelegation":
                return PoolType.PASSIVE_DELEGATION;
            default:
                throw new IllegalArgumentException("Invalid Pool Type " + value);
        }
    }
}
