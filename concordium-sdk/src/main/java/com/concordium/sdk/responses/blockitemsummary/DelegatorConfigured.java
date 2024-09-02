package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.grpc.v2.DelegationEvent;
import com.concordium.sdk.responses.transactionstatus.*;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

import java.util.List;

/**
 * Delegation was configured for the sender of the transaction.
 * The sender has sent a {@link com.concordium.sdk.transactions.ConfigureDelegation} transaction.
 */
@EqualsAndHashCode
@Getter
@Builder
public class DelegatorConfigured {

    @Singular
    private final List<AbstractDelegatorResult> events;

    public static DelegatorConfigured from(AccountTransactionEffects.DelegationConfigured delegationConfigured, AccountAddress sender) {
        val builder = DelegatorConfigured.builder();
        val delegationEvents = delegationConfigured.getEventsList();
        for (DelegationEvent delegationEvent : delegationEvents) {
            switch (delegationEvent.getEventCase()) {
                case DELEGATION_STAKE_INCREASED:
                    builder.event(DelegationStakeIncreased.from(delegationEvent.getDelegationStakeIncreased(), sender));
                    break;
                case DELEGATION_STAKE_DECREASED:
                    builder.event(DelegationStakeDecreased.from(delegationEvent.getDelegationStakeDecreased(), sender));
                    break;
                case DELEGATION_SET_RESTAKE_EARNINGS:
                    builder.event(DelegationSetRestakeEarnings.from(delegationEvent.getDelegationSetRestakeEarnings(), sender));
                    break;
                case DELEGATION_SET_DELEGATION_TARGET:
                    builder.event(DelegationSetDelegationTarget.from(delegationEvent.getDelegationSetDelegationTarget(), sender));
                    break;
                case DELEGATION_ADDED:
                    builder.event(DelegationAdded.from(delegationEvent.getDelegationAdded(), sender));
                    break;
                case DELEGATION_REMOVED:
                    builder.event(DelegationRemoved.from(delegationEvent.getDelegationRemoved(), sender));
                    break;
                case BAKER_REMOVED:
                    builder.event(DelegationBakerRemoved.from(delegationEvent.getBakerRemoved(), sender));
                case EVENT_NOT_SET:
                    throw new IllegalArgumentException("Delegation event type not set");
            }
        }
        return builder.build();
    }
}
