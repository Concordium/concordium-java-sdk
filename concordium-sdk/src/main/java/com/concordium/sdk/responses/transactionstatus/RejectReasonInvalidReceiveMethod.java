package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
class RejectReasonInvalidReceiveMethod extends RejectReasonContent {
    private final String moduleRef;
    private final String receiveName;

    @JsonCreator
    public RejectReasonInvalidReceiveMethod(@JsonProperty("moduleRef") String moduleRef,
                                            @JsonProperty("receiveName") String receiveName) {
        this.moduleRef = moduleRef;
        this.receiveName = receiveName;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_RECEIVE_METHOD;
    }
}
