package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Rejected due to contract logic in the init method of the contract.
 */
@Getter
@ToString
@Builder
public class RejectReasonRejectedInit extends RejectReason {
    /**
     * The reject reason code.
     */
    private final int rejectedInit;
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REJECTED_INIT;
    }
}
