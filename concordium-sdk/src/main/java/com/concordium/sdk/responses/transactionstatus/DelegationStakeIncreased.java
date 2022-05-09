package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DelegationStakeIncreased extends AbstractDelegatorResult {
    private final String newStake;

    @JsonCreator
    DelegationStakeIncreased(@JsonProperty("delegatorId") String delegatorId,
                             @JsonProperty("account") AccountAddress delegatorAddress,
                             @JsonProperty("newStake") String newStake) {
        super(delegatorId, delegatorAddress);
        this.newStake = newStake;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_STAKE_INCREASED;
    }
}
