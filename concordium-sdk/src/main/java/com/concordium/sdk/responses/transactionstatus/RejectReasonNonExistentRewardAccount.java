package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class RejectReasonNonExistentRewardAccount extends RejectReasonContent{
    @Getter
    private final AccountAddress address;

    @JsonCreator
    RejectReasonNonExistentRewardAccount(@JsonProperty("address") AccountAddress address) {
        this.address = address;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_REWARD_ACCOUNT;
    }
}
