package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Reference to a non-existing module.
 */
@ToString
@Builder
public class RejectReasonInvalidModuleReference extends RejectReason {
    @Getter
    private final String moduleRef;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_MODULE_REFERENCE;
    }
}
