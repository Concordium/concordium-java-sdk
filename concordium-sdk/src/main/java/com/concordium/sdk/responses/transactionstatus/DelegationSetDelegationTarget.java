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
 * The delegator set a target for delegation.
 */
@ToString
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DelegationSetDelegationTarget extends AbstractDelegatorResult {

    /**
     * The target to delegate to.
     */
    private final DelegationTarget delegationTarget;

    @JsonCreator
    DelegationSetDelegationTarget(@JsonProperty("delegatorId") AccountIndex delegatorId,
                                  @JsonProperty("account") AccountAddress delegatorAddress,
                                  @JsonProperty("delegationTarget") DelegationTarget delegationTarget) {
        super(delegatorId, delegatorAddress);
        this.delegationTarget = delegationTarget;
    }

    public static DelegationSetDelegationTarget from(DelegationEvent.DelegationSetDelegationTarget delegationSetDelegationTarget, AccountAddress sender) {
        return DelegationSetDelegationTarget
                .builder()
                .delegatorId(AccountIndex.from(delegationSetDelegationTarget.getDelegatorId()))
                .delegatorAddress(sender)
                .delegationTarget(DelegationTarget.from(delegationSetDelegationTarget.getDelegationTarget()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_SET_DELEGATION_TARGET;
    }
}