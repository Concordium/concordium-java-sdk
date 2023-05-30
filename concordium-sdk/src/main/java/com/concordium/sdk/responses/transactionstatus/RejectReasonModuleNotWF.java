package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Error raised when validating the Wasm module.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonModuleNotWF extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_NOT_WF;
    }
}
