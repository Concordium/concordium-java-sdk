package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DelegationStakeDecreased extends AbstractDelegatorResult {
    private final String newStake;

    @JsonCreator
    DelegationStakeDecreased(@JsonProperty("delegatorId") String delegatorId,
                             @JsonProperty("account") String delegatorAddress,
                             @JsonProperty("newStake") String newStake) {
        super(delegatorId, delegatorAddress);
        this.newStake = newStake;
    }
}
