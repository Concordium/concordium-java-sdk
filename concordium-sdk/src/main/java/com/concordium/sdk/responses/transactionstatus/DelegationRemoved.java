package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Delegator stopped its delegation.
 */
@Getter
@Setter
public class DelegationRemoved extends AbstractDelegatorResult {

    @JsonCreator
    DelegationRemoved(@JsonProperty("delegatorId") AccountIndex delegatorId,
                      @JsonProperty("account") AccountAddress delegatorAddress) {
        super(delegatorId, delegatorAddress);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_REMOVED;
    }
}
