package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DelegationAdded extends AbstractDelegatorResult {


    @JsonCreator
    DelegationAdded(@JsonProperty("delegatorId") String delegatorId,
                    @JsonProperty("account") String delegatorAddress) {
        super(delegatorId, delegatorAddress);
    }
}
