package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BakerSetBakingRewardCommission extends AbstractBakerResult {
    // parts per 100000
    private final int bakingRewardCommission;

    @JsonCreator
    BakerSetBakingRewardCommission(@JsonProperty("bakerId") String bakerId,
                                   @JsonProperty("account") AccountAddress bakerAccount,
                                   @JsonProperty("bakingRewardCommission") int bakingRewardCommission) {
        super(bakerId, bakerAccount);
        this.bakingRewardCommission = bakingRewardCommission;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_BAKING_REWARD_COMMISSION;
    }
}
