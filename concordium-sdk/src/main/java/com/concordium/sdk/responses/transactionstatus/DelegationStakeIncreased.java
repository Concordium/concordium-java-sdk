package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.DelegationEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The delegator increased its stake.
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DelegationStakeIncreased extends AbstractDelegatorResult {

    /**
     * The new stake
     */
    private final CCDAmount newStake;

    public static DelegationStakeIncreased from(DelegationEvent.DelegationStakeIncreased delegationStakeIncreased, AccountAddress address) {
        return DelegationStakeIncreased
                .builder()
                .delegatorId(AccountIndex.from(delegationStakeIncreased.getDelegatorId()))
                .delegatorAddress(address)
                .newStake(CCDAmount.from(delegationStakeIncreased.getNewStake()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_STAKE_INCREASED;
    }
}
