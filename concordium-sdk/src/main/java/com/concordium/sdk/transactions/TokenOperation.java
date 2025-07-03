package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;

public interface TokenOperation {

    UInt64 getBaseCost();

    Object toCbor();
}
