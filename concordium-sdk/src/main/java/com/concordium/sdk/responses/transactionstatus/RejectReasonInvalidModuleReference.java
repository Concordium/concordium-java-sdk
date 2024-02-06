package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.modulelist.ModuleRef;
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
    private final ModuleRef moduleRef;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_MODULE_REFERENCE;
    }
}
