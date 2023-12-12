package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Reward account desired by the baker does not exist.
 */
@ToString
@Builder
public class RejectReasonNonExistentRewardAccount extends RejectReason {
    @Getter
    private final AccountAddress address;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_REWARD_ACCOUNT;
    }
}
