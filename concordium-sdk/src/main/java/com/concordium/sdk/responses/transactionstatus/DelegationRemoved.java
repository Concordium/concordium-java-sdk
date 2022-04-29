package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelegationRemoved extends AbstractDelegatorResult {

    @JsonCreator
    DelegationRemoved(@JsonProperty("delegatorId") String delegatorId,
                    @JsonProperty("account") String delegatorAddress) {
        super(delegatorId, delegatorAddress);
    }
}
