package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RejectReasonModuleHashAlreadyExists extends RejectReasonContent {
    private final String moduleRef;

    @JsonCreator
    RejectReasonModuleHashAlreadyExists(@JsonProperty("moduleRef") String moduleRef) {
        this.moduleRef = moduleRef;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_HASH_ALREADY_EXISTS;
    }
}
