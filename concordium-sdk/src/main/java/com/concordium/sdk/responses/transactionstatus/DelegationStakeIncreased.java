package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * The delegator increased its stake.
 */
@Getter
@ToString
public class DelegationStakeIncreased extends AbstractDelegatorResult {

    /**
     * The new stake
     */
    private final CCDAmount newStake;

    @JsonCreator
    DelegationStakeIncreased(@JsonProperty("delegatorId") AccountIndex delegatorId,
                             @JsonProperty("account") AccountAddress delegatorAddress,
                             @JsonProperty("newStake") CCDAmount newStake) {
        super(delegatorId, delegatorAddress);
        this.newStake = newStake;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_STAKE_INCREASED;
    }
}
