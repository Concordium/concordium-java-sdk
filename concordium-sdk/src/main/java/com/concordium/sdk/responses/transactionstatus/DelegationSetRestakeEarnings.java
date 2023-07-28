package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegationEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The delegator set its restake property.
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DelegationSetRestakeEarnings extends AbstractDelegatorResult {


    /**
     * Whether earnings should be automatically restaked or not.
     */
    private final boolean restakeEarnings;

    @JsonCreator
    DelegationSetRestakeEarnings(@JsonProperty("delegatorId") AccountIndex delegatorId,
                                 @JsonProperty("account") AccountAddress delegatorAddress,
                                 @JsonProperty("restakeEarnings") boolean restakeEarnings) {
        super(delegatorId, delegatorAddress);
        this.restakeEarnings = restakeEarnings;
    }

    public static DelegationSetRestakeEarnings from(DelegationEvent.DelegationSetRestakeEarnings delegationSetRestakeEarnings, AccountAddress sender) {
        return DelegationSetRestakeEarnings
                .builder()
                .delegatorId(AccountIndex.from(delegationSetRestakeEarnings.getDelegatorId()))
                .delegatorAddress(sender)
                .restakeEarnings(delegationSetRestakeEarnings.getRestakeEarnings())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_SET_RESTAKE_EARNINGS;
    }
}
