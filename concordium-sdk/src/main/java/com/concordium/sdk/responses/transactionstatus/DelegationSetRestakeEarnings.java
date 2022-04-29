package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DelegationSetRestakeEarnings extends AbstractDelegatorResult {

    private final boolean restakeEarnings;

    @JsonCreator
    DelegationSetRestakeEarnings(@JsonProperty("delegatorId") String delegatorId,
                                 @JsonProperty("account") String delegatorAddress,
                                 @JsonProperty("restakeEarnings") boolean restakeEarnings) {
       super(delegatorId, delegatorAddress);
        this.restakeEarnings = restakeEarnings;
    }
}
