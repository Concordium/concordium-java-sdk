package com.concordium.sdk.responses.poolstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PoolType {
    BAKER_POOL,
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
