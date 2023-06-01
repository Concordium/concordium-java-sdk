package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
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
        delegationEvents.forEach(delegationEvent -> events.add(DelegationEvent.parse(delegationEvent, sender)));

        return DelegationConfiguredResult.builder()
                .events(events.build())
                .build();
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.CONFIGURE_DELEGATION;
    }
}
