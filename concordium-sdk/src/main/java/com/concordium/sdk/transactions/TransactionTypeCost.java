package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.Getter;

public enum TransactionTypeCost {
    CONFIGURE_BAKER(300),
    CONFIGURE_BAKER_WITH_PROOFS(4050),
    CONFIGURE_DELEGATION(500);

    @Getter
    private final UInt64 value;

    TransactionTypeCost(int energy) {
        this.value = UInt64.from(energy);
    }

}
