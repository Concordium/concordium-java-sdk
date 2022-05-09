package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class BakerStakeIncreasedResult extends AbstractBakerResult {
    private final String newStake;

    @JsonCreator
    BakerStakeIncreasedResult(@JsonProperty("bakerId") String bakerId,
                              @JsonProperty("account") String account,
                              @JsonProperty("newStake") String newStake) {
        super(bakerId, account);
        this.newStake = newStake;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_STAKE_INCREASED;
    }
}
