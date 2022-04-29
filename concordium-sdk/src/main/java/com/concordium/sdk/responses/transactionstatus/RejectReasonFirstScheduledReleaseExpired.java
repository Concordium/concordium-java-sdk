package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The first scheduled release in a transfer with schedule has already expired.
 */
@ToString
public class RejectReasonFirstScheduledReleaseExpired extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.FIRST_SCHEDULED_RELEASE_EXPIRED;
    }
}
