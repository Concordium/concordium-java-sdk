package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * Error raised when validating the Wasm module.
 */
@ToString
public class RejectReasonModuleNotWF extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_NOT_WF;
    }
}
