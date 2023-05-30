package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegationEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The delegator increased its stake.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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

    /**
     * Parses {@link DelegationEvent.DelegationStakeIncreased} and {@link com.concordium.grpc.v2.AccountAddress} to {@link DelegationStakeIncreased}.
     * @param delegationStakeIncreased {@link DelegationEvent.DelegationStakeIncreased} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationStakeIncreased}.
     */
    public static DelegationStakeIncreased parse(DelegationEvent.DelegationStakeIncreased delegationStakeIncreased, com.concordium.grpc.v2.AccountAddress sender) {
        return DelegationStakeIncreased.builder()
                .delegatorId(AccountIndex.from(delegationStakeIncreased.getDelegatorId().getId().getValue()))
                .delegatorAddress(AccountAddress.parse(sender))
                .newStake(CCDAmount.fromMicro(delegationStakeIncreased.getNewStake().getValue()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_STAKE_INCREASED;
    }
}
