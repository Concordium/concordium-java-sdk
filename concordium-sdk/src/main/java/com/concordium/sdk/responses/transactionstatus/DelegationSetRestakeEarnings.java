package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * The delegator set its restake property.
 */
@Getter
@ToString
public class DelegationSetRestakeEarnings extends AbstractDelegatorResult {


    /**
     * Whether earnings should be automatically restaked or not.
     */
    private final boolean restakeEarnings;

    @JsonCreator
    DelegationSetRestakeEarnings(@JsonProperty("delegatorId") long delegatorId,
                                 @JsonProperty("account") AccountAddress delegatorAddress,
                                 @JsonProperty("restakeEarnings") boolean restakeEarnings) {
       super(delegatorId, delegatorAddress);
        this.restakeEarnings = restakeEarnings;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_SET_RESTAKE_EARNINGS;
    }
}
