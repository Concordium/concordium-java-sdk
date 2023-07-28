package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegationEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DelegationStakeDecreased extends AbstractDelegatorResult {
    private final CCDAmount newStake;

    @JsonCreator
    DelegationStakeDecreased(@JsonProperty("delegatorId") AccountIndex delegatorId,
                             @JsonProperty("account") AccountAddress delegatorAddress,
                             @JsonProperty("newStake") String newStake) {
        super(delegatorId, delegatorAddress);
        this.newStake = CCDAmount.fromMicro(newStake);
    }

    public static DelegationStakeDecreased from(DelegationEvent.DelegationStakeDecreased delegationStakeDecreased, AccountAddress sender) {
        return DelegationStakeDecreased
                .builder()
                .delegatorId(AccountIndex.from(delegationStakeDecreased.getDelegatorId()))
                .delegatorAddress(sender)
                .newStake(CCDAmount.from(delegationStakeDecreased.getNewStake()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_STAKE_DECREASED;
    }
}
