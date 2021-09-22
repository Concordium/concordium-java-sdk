package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class RejectReasonAlreadyABaker extends RejectReasonContent {
    @Getter
    private final UInt64 bakerId;

    @JsonCreator
    RejectReasonAlreadyABaker(@JsonProperty("bakerId") long bakerId) {
        this.bakerId = UInt64.from(bakerId);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ALREADY_A_BAKER;
    }
}
