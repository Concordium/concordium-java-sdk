package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
public class RejectReasonNonExistentRewardAccount extends RejectReason {
    @Getter
    private final AccountAddress address;

    @JsonCreator
    RejectReasonNonExistentRewardAccount(@JsonProperty("contents") String address) {
        this.address = AccountAddress.from(address);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_REWARD_ACCOUNT;
    }
}
