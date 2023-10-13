package com.concordium.sdk.transactions.smartcontracts;

import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.transactions.InitContractPayload;
import com.concordium.sdk.transactions.UpdateContractPayload;

/**
 * The possible variants of {@link SchemaParameter}.
 */
public enum ParameterType {
    /**
     * A Parameter for {@link InitContractPayload}.
     */
    INIT,
    /**
     * A Parameter for {@link UpdateContractPayload} or {@link InvokeInstanceRequest}.
     */
    RECEIVE
}
