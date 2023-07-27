package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Reward account desired by the baker does not exist.
 */
@ToString
public class RejectReasonNonExistentRewardAccount extends RejectReason {
    @Getter
    private final AccountAddress address;

    @JsonCreator
    RejectReasonNonExistentRewardAccount(@JsonProperty("contents") AccountAddress address) {
        this.address = address;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_REWARD_ACCOUNT;
    }
}
