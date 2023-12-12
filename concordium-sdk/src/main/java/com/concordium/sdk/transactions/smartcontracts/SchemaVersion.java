package com.concordium.sdk.transactions.smartcontracts;

import lombok.Getter;

/**
 * The <a href="https://docs.rs/concordium-contracts-common/latest/concordium_contracts_common/schema/index.html#">version</a> of the {@link Schema}.
 * If this is updated, the corresponding code in the rust layer must also be updated accordingly to ensure correct deserialization.
  */
@Getter
public enum SchemaVersion {
    V0(0),
    V1(1),
    V2(2),
    V3(3),
    UNKNOWN(-1);

    /**
     * The default Schema Version. Used when instantiating {@link Schema} if no specific version is provided.
     */
    public static final SchemaVersion DEFAULT = V3;

    private final int version;

    SchemaVersion(int version) {
        this.version = version;
    }
}
