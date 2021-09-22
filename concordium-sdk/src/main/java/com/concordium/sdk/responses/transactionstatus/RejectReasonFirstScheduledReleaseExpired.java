package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonFirstScheduledReleaseExpired extends RejectReasonContent{
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.FIRST_SCHEDULED_RELEASE_EXPIRED;
    }
}
