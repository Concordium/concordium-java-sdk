package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.transactions.InitContract;
import com.concordium.sdk.transactions.UpdateContract;

/**
 * The possible variants of {@link SchemaParameter}.
 */
public enum ParameterType {
    /**
     * A Parameter for {@link InitContract}.
     */
    INIT,
    /**
     * A Parameter for {@link UpdateContract} or {@link InvokeInstanceRequest}.
     */
    RECEIVE
}
