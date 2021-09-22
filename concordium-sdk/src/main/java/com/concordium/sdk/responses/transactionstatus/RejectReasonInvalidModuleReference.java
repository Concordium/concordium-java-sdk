package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class RejectReasonInvalidModuleReference extends RejectReasonContent {
    @Getter
    private final String moduleRef;

    @JsonCreator
    RejectReasonInvalidModuleReference(@JsonProperty("moduleRef") String moduleRef) {
        this.moduleRef = moduleRef;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_MODULE_REFERENCE;
    }
}
