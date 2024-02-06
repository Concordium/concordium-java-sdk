package com.concordium.sdk.responses.smartcontracts;


/**
 * Contract trace element types used with the GRPCv2 API.
 */
public enum ContractTraceElementType {
    /**
     * A contract instance was updated.
     * This event type corresponds to the concrete event {@link com.concordium.sdk.responses.transactionstatus.ContractUpdated}
     */
    INSTANCE_UPDATED,
    /**
     * A contract to account transfer occurred.
     * This event type corresponds to the concrete event {@link com.concordium.sdk.responses.transactionstatus.TransferredResult}.
     */
    TRANSFERRED,
    /**
     * A contract was interrupted.
     * This event type corresponds to the concrete event {@link com.concordium.sdk.responses.transactionstatus.InterruptedResult}.
     */
    INTERRUPTED,
    /**
     * A previously interrupted contract was resumed.
     * This event type corresponds to the concrete event {@link com.concordium.sdk.responses.transactionstatus.ResumedResult}.
     */
    RESUMED,
    /**
     * A contract was successfully upgraded.
     * This event type corresponds to the concrete event {@link com.concordium.sdk.responses.transactionstatus.UpgradedResult}.
     */
    UPGRADED
}
