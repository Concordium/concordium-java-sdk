package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegationEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The delegator set a target for delegation.
 */
@ToString
@Getter
@SuperBuilder
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

    /**
     * Parses {@link DelegationEvent.DelegationSetDelegationTarget} and {@link com.concordium.grpc.v2.AccountAddress} to {@link DelegationSetDelegationTarget}.
     * @param delegationSetDelegationTarget {@link DelegationEvent.DelegationSetDelegationTarget} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationSetDelegationTarget}.
     */
    public static DelegationSetDelegationTarget parse(DelegationEvent.DelegationSetDelegationTarget delegationSetDelegationTarget, com.concordium.grpc.v2.AccountAddress sender) {
        return DelegationSetDelegationTarget.builder()
                .delegatorId(AccountIndex.from(delegationSetDelegationTarget.getDelegatorId().getId().getValue()))
                .delegatorAddress(AccountAddress.parse(sender))
                .delegationTarget(DelegationTarget.parse(delegationSetDelegationTarget.getDelegationTarget()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_SET_DELEGATION_TARGET;
    }
}