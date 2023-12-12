package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * The module already exists.
 */
@Getter
@ToString
@Builder
public class RejectReasonModuleHashAlreadyExists extends RejectReason {
    private final String moduleRef;
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_HASH_ALREADY_EXISTS;
    }
}
