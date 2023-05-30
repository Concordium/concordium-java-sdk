package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Runtime exception occurred when running either the init or receive method of the contract.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonRuntimeFailure extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.RUNTIME_FAILURE;
    }
}
