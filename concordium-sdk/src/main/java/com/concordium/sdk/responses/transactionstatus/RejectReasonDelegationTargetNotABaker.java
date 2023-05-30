package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerId;
import com.concordium.sdk.responses.AccountIndex;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Delegation target is not a baker
 */
@ToString
@Getter
@EqualsAndHashCode
public class RejectReasonDelegationTargetNotABaker extends RejectReason {
    /**
     * The delegation target which was not a baker.
     */
    private final AccountIndex bakerId;

    @JsonCreator
    RejectReasonDelegationTargetNotABaker(@JsonProperty("contents") AccountIndex bakerId) {
        this.bakerId = bakerId;
    }

    /**
     * Parses {@link BakerId} to {@link DelegationTarget}.
     * @param delegationTargetNotABaker {@link BakerId} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonDelegationTargetNotABaker}.
     */
    public static RejectReasonDelegationTargetNotABaker parse(BakerId delegationTargetNotABaker) {
        return new RejectReasonDelegationTargetNotABaker(AccountIndex.from(delegationTargetNotABaker.getValue()));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DELEGATION_TARGET_NOT_A_BAKER;
    }
}
