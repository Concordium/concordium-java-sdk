package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Delegation target is not a baker
 */
@ToString
@Getter
public class RejectReasonDelegationTargetNotABaker extends RejectReason {
    /**
     * The delegation target which was not a baker.
     */
    private final AccountIndex bakerId;

    @JsonCreator
    RejectReasonDelegationTargetNotABaker(@JsonProperty("contents") AccountIndex bakerId) {
        this.bakerId = bakerId;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DELEGATION_TARGET_NOT_A_BAKER;
    }
}
