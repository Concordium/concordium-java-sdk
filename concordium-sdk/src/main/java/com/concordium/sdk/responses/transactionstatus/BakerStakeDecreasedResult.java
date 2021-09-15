package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class BakerStakeDecreasedResult extends AbstractBakerResult {
    private final String newStake;

    @JsonCreator
    BakerStakeDecreasedResult(@JsonProperty("bakerId") String bakerId,
                              @JsonProperty("account") String account,
                              @JsonProperty("newStake") String newStake) {
        super(bakerId, account);
        this.newStake = newStake;
    }
}
