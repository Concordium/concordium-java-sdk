package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Delegation target is not a baker
 */
@ToString
@Getter
public class RejectReasonDelegationTargetNotABaker {
    /**
     * The delegation target which was not a baker.
     */
    private final UInt64 bakerId;

    @JsonCreator
    RejectReasonDelegationTargetNotABaker(@JsonProperty("contents") long bakerId) {
        this.bakerId = UInt64.from(bakerId);
    }
}
