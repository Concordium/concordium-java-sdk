package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Reference to a non-existing module.
 */
@ToString
public class RejectReasonInvalidModuleReference extends RejectReason {
    @Getter
    private final String moduleRef;

    @JsonCreator
    RejectReasonInvalidModuleReference(@JsonProperty("contents") String moduleRef) {
        this.moduleRef = moduleRef;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_MODULE_REFERENCE;
    }
}
