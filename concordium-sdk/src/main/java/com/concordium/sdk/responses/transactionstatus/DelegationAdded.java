package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * The sender of the transaction has started delegating.
 */
@Getter
@ToString
public class DelegationAdded extends AbstractDelegatorResult {


    @JsonCreator
    DelegationAdded(@JsonProperty("delegatorId") String delegatorId,
                    @JsonProperty("account") AccountAddress delegatorAddress) {
        super(delegatorId, delegatorAddress);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_ADDED;
    }
}
