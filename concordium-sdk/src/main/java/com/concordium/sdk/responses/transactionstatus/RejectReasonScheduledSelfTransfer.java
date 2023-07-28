package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Account tried to transfer with schedule to itself, that's not allowed.
 */
@ToString
public class RejectReasonScheduledSelfTransfer extends RejectReason {
    @Getter
    private final AccountAddress accountAddress;

    @JsonCreator
    RejectReasonScheduledSelfTransfer(@JsonProperty("contents") AccountAddress accountAddress) {
        this.accountAddress = accountAddress;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SCHEDULED_SELF_TRANSFER;
    }
}
