package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Error raised when validating a Wasm module that is not well formed.
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class RejectReasonModuleNotWF extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_NOT_WF;
    }
}
