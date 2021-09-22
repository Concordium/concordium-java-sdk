package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class RejectReasonScheduledSelfTransfer extends RejectReasonContent {
    @Getter
    private final AccountAddress accountAddress;

    @JsonCreator
    RejectReasonScheduledSelfTransfer(@JsonProperty("accountAddress") String accountAddress) {
        this.accountAddress = AccountAddress.from(accountAddress);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SCHEDULED_SELF_TRANSFER;
    }
}
