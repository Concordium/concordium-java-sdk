package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonFirstScheduledReleaseExpired extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.FIRST_SCHEDULED_RELEASE_EXPIRED;
    }
}
