package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * The delegator set a target for delegation.
 */
@ToString
@Getter
public class DelegationSetDelegationTarget extends AbstractDelegatorResult {

    /**
     * The target to delegate to.
     */
    private final DelegationTarget delegationTarget;

    @JsonCreator
    DelegationSetDelegationTarget(@JsonProperty("delegatorId") String delegatorId,
                                  @JsonProperty("account") AccountAddress delegatorAddress,
                                  @JsonProperty("delegationTarget") DelegationTarget delegationTarget) {
        super(delegatorId, delegatorAddress);
        this.delegationTarget = delegationTarget;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_SET_DELEGATION_TARGET;
    }
}