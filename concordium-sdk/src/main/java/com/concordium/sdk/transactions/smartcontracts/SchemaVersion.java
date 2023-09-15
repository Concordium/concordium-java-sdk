package com.concordium.sdk.transactions.smartcontracts;

import lombok.Getter;

/**
 * TODO comment. Where are the versions coming from (link?)
 */
@Getter
public enum SchemaVersion {
    V0(0),
    V1(1),
    V2(2),
    V3(3),
    UNKNOWN(-1);

    private final int version;
    SchemaVersion(int version) {
        this.version = version;
    }
}
