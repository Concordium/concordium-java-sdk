package com.concordium.sdk.transactions.smartcontracts;

import lombok.Getter;

/**
 * The <a href="https://docs.rs/concordium-contracts-common/latest/concordium_contracts_common/schema/index.html#">version</a> of the {@link Schema}.
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
