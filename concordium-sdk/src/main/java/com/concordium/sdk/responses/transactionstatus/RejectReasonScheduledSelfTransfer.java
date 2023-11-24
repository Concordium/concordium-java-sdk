package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Account tried to transfer with schedule to itself, that's not allowed.
 */
@ToString
@Builder
public class RejectReasonScheduledSelfTransfer extends RejectReason {
    @Getter
    private final AccountAddress accountAddress;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SCHEDULED_SELF_TRANSFER;
    }
}
