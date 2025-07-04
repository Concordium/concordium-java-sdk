package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.transactions.TokenUpdate;
import com.concordium.sdk.types.UInt64;

/**
 * A CBOR-serializable (Jackson) protocol-level token (PLT) operation,
 * used in {@link TokenUpdate}.
 */
public interface TokenOperation {

    /**
     * @return the base energy cost of this operation.
     */
    UInt64 getBaseCost();
}
