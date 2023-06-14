package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.sdk.responses.transactionstatus.*;

/**
 * Contract trace element types used with the GRPCv2 API.
 */
public enum ContractTraceElementType {
    /**
     * A contract instance was updated.
     * This event type corresponds to the concrete event {@link ContractUpdated}
     */
    INSTANCE_UPDATED,
    /**
     * A contract to account transfer occurred.
     * This event type corresponds to the concrete event {@link TransferredResult}.
     */
    TRANSFERRED,
    /**
     * A contract was interrupted.
     * This event type corresponds to the concrete event {@link InterruptedResult}.
     */
    INTERRUPTED,
    /**
     * A previously interrupted contract was resumed.
     * This event type corresponds to the concrete event {@link ResumedResult}.
     */
    RESUMED,
    /**
     * A contract was successfully upgraded.
     * This event type corresponds to the concrete event {@link UpgradedResult}.
     */
    UPGRADED
}
