package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerStakeUpdatedData;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class BakerStakeIncreasedResult extends AbstractBakerResult {
    private final String newStake;

    @JsonCreator
    BakerStakeIncreasedResult(@JsonProperty("bakerId") AccountIndex bakerId,
                              @JsonProperty("account") AccountAddress account,
                              @JsonProperty("newStake") String newStake) {
        super(bakerId, account);
        this.newStake = newStake;
    }

    // TODO
    public static BakerStakeIncreasedResult parse(BakerStakeUpdatedData update) {
        return null;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_STAKE_INCREASED;
    }
}
