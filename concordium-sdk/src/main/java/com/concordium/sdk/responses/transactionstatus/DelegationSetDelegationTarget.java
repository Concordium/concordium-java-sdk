package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DelegationSetDelegationTarget extends AbstractDelegatorResult {
    private final DelegationTarget delegationTarget;

    @JsonCreator
    DelegationSetDelegationTarget(@JsonProperty("delegatorId") String delegatorId,
                                  @JsonProperty("account") AccountAddress delegatorAddress,
                                  @JsonProperty("delegationTarget") DelegationTarget delegationTarget) {
        super(delegatorId, delegatorAddress);
        this.delegationTarget = delegationTarget;
    }

}