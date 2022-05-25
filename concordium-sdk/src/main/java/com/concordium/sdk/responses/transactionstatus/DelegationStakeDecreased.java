package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DelegationStakeDecreased extends AbstractDelegatorResult {
    private final String newStake;

    @JsonCreator
    DelegationStakeDecreased(@JsonProperty("delegatorId") AccountIndex delegatorId,
                             @JsonProperty("account") AccountAddress delegatorAddress,
                             @JsonProperty("newStake") String newStake) {
        super(delegatorId, delegatorAddress);
        this.newStake = newStake;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_STAKE_DECREASED;
    }
}
