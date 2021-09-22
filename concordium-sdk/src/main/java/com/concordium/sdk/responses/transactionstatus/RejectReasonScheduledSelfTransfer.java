package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
public class RejectReasonScheduledSelfTransfer extends RejectReason {
    @Getter
    private final AccountAddress accountAddress;

    @JsonCreator
    RejectReasonScheduledSelfTransfer(@JsonProperty("contents") String accountAddress) {
        this.accountAddress = AccountAddress.from(accountAddress);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SCHEDULED_SELF_TRANSFER;
    }
}
