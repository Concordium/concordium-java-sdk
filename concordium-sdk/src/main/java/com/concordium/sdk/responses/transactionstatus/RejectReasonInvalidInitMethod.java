package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RejectReasonInvalidInitMethod extends RejectReasonContent {
    private final String moduleRef;
    private final String initName;

    @JsonCreator
    RejectReasonInvalidInitMethod(@JsonProperty("moduleRef") String moduleRef,
                                  @JsonProperty("initName") String initName) {
        this.moduleRef = moduleRef;
        this.initName = initName;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INIT_METHOD;
    }
}
