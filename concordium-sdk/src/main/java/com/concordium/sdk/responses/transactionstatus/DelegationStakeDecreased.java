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
 * The delegator decreased its stake.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder
public class DelegationStakeDecreased extends AbstractDelegatorResult {

    /**
     * The new stake.
     */
    private final CCDAmount newStake;

    @JsonCreator
    DelegationStakeDecreased(@JsonProperty("delegatorId") AccountIndex delegatorId,
                             @JsonProperty("account") AccountAddress delegatorAddress,
                             @JsonProperty("newStake") String newStake) {
        super(delegatorId, delegatorAddress);
        this.newStake = CCDAmount.fromMicro(newStake);
    }

    /**
     * Parses {@link DelegationEvent.DelegationStakeDecreased} and {@link com.concordium.grpc.v2.AccountAddress} to {@link DelegationStakeDecreased}.
     * @param delegationStakeDecreased {@link DelegationEvent.DelegationStakeDecreased} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationStakeDecreased}.
     */
    public static DelegationStakeDecreased parse(DelegationEvent.DelegationStakeDecreased delegationStakeDecreased, com.concordium.grpc.v2.AccountAddress sender) {
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
}
