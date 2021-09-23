package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RejectReasonRejectedInit extends RejectReason {
    private final int rejectedInit;

    @JsonCreator
    public RejectReasonRejectedInit(@JsonProperty("rejectReason") int rejectedInit) {
        this.rejectedInit = rejectedInit;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REJECTED_INIT;
    }
}
