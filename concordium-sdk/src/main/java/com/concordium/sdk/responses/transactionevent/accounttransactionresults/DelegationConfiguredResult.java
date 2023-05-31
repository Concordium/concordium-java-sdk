package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.*;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * An account configured delegation.
 * The details of what happened are stored in the list of {@link DelegationEvent}
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class DelegationConfiguredResult implements AccountTransactionResult {

    private List<DelegationEvent> events;

    public static DelegationConfiguredResult parse(AccountTransactionEffects.DelegationConfigured delegationConfigured, AccountAddress sender) {
        val events = new ImmutableList.Builder<DelegationEvent>();
        val delegationEvents = delegationConfigured.getEventsList();
        delegationEvents.forEach(e -> {
            switch (e.getEventCase()) {
                case DELEGATION_STAKE_INCREASED:
                    events.add(DelegationStakeIncreased.parse(e.getDelegationStakeIncreased(), sender));
                    break;
                case DELEGATION_STAKE_DECREASED:
                    events.add(DelegationStakeDecreased.parse(e.getDelegationStakeDecreased(), sender));
                    break;
                case DELEGATION_SET_RESTAKE_EARNINGS:
                    events.add(DelegationSetRestakeEarnings.parse(e.getDelegationSetRestakeEarnings(), sender));
                    break;
                case DELEGATION_SET_DELEGATION_TARGET:
                    events.add(DelegationSetDelegationTarget.parse(e.getDelegationSetDelegationTarget(), sender));
                    break;
                case DELEGATION_ADDED:
                    events.add(DelegationAdded.parse(e.getDelegationAdded(), sender));
                    break;
                case DELEGATION_REMOVED:
                    events.add(DelegationRemoved.parse(e.getDelegationRemoved(), sender));
                    break;
                case EVENT_NOT_SET:
                    throw new IllegalArgumentException();
            }
        });
        return DelegationConfiguredResult.builder()
                .events(events.build())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.DELEGATION_CONFIGURED;
    }
}
