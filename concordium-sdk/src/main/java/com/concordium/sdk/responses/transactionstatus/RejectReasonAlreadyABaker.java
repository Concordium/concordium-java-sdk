package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Tried to add baker/delegator for an account that already has a baker.
 */
@ToString
@Getter
public class RejectReasonAlreadyABaker extends RejectReason {
    private final UInt64 bakerId;

    @JsonCreator
    RejectReasonAlreadyABaker(@JsonProperty("contents") long bakerId) {
        this.bakerId = UInt64.from(bakerId);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ALREADY_A_BAKER;
    }
}
