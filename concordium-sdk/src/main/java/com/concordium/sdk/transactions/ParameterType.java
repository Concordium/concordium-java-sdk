package com.concordium.sdk.transactions;

import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;

/**
 * The possible variants of {@link SchemaParameter}.
 */
public enum ParameterType {
    /**
     * The parameter is to be used with {@link InitContractPayload}.
     */
    INIT,
    /**
     * The parameter is to be used with {@link UpdateContractPayload} or {@link InvokeInstanceRequest}.
     */
    RECEIVE
}
