package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Delegation target is not a baker
 */
@ToString
@Getter
@Builder
public class RejectReasonDelegationTargetNotABaker extends RejectReason {
    /**
     * The delegation target which was not a baker.
     */
    private final AccountIndex bakerId;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DELEGATION_TARGET_NOT_A_BAKER;
    }
}
