package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.DelegationEvent;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.DelegationEventType;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The delegator decreased its stake.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class DelegationStakeDecreased extends AbstractDelegatorResult implements DelegationEvent {

    /**
     * The new stake.
     */
    private final CCDAmount newStake;

    @Builder
    @JsonCreator
    DelegationStakeDecreased(@JsonProperty("delegatorId") AccountIndex delegatorId,
                             @JsonProperty("account") AccountAddress delegatorAddress,
                             @JsonProperty("newStake") CCDAmount newStake) {
        super(delegatorId, delegatorAddress);
        this.newStake = newStake;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.DelegationEvent.DelegationStakeDecreased} and {@link com.concordium.grpc.v2.AccountAddress} to {@link DelegationStakeDecreased}.
     * @param delegationStakeDecreased {@link com.concordium.grpc.v2.DelegationEvent.DelegationStakeDecreased} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationStakeDecreased}.
     */
    public static DelegationStakeDecreased parse(com.concordium.grpc.v2.DelegationEvent.DelegationStakeDecreased delegationStakeDecreased, com.concordium.grpc.v2.AccountAddress sender) {
        return DelegationStakeDecreased.builder()
                .delegatorId(AccountIndex.from(delegationStakeDecreased.getDelegatorId().getId().getValue()))
                .delegatorAddress(AccountAddress.parse(sender))
                .newStake(CCDAmount.fromMicro(delegationStakeDecreased.getNewStake().getValue()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_STAKE_DECREASED;
    }

    @Override
    public DelegationEventType getDelegationEventType() {
        return DelegationEventType.DELEGATION_STAKE_DECREASED;
    }
}
