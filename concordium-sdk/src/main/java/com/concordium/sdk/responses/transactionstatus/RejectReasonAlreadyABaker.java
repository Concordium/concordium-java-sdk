package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Tried to add baker/delegator for an account that already has a baker.
 */
@ToString
@Getter
@Builder
public class RejectReasonAlreadyABaker extends RejectReason {
    private final BakerId bakerId;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ALREADY_A_BAKER;
    }
}
