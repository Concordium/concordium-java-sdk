package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * The module already exists
 */
@Getter
@ToString
public class RejectReasonModuleHashAlreadyExists extends RejectReason {
    private final String moduleRef;

    @JsonCreator
    RejectReasonModuleHashAlreadyExists(@JsonProperty("contents") String moduleRef) {
        this.moduleRef = moduleRef;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_HASH_ALREADY_EXISTS;
    }
}
