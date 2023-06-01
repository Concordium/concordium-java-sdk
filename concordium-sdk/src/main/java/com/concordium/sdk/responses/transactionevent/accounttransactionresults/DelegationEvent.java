package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.sdk.responses.transactionstatus.*;

public interface DelegationEvent {

    DelegationEventType getDelegationEventType();

    /**
     * Parses {@link com.concordium.grpc.v2.DelegationEvent} and {@link AccountAddress} to {@link DelegationEvent}.
     * @param delegationEvent {@link com.concordium.grpc.v2.DelegationEvent} returned by the GRPC V2 API.
     * @param sender {@link AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link DelegationEvent}.
     */
    static DelegationEvent parse(com.concordium.grpc.v2.DelegationEvent delegationEvent, AccountAddress sender) {
        switch (delegationEvent.getEventCase()) {
            case DELEGATION_STAKE_INCREASED:
                return DelegationStakeIncreased.parse(delegationEvent.getDelegationStakeIncreased(), sender);
            case DELEGATION_STAKE_DECREASED:
                return DelegationStakeDecreased.parse(delegationEvent.getDelegationStakeDecreased(), sender);
            case DELEGATION_SET_RESTAKE_EARNINGS:
                return DelegationSetRestakeEarnings.parse(delegationEvent.getDelegationSetRestakeEarnings(), sender);
            case DELEGATION_SET_DELEGATION_TARGET:
                return DelegationSetDelegationTarget.parse(delegationEvent.getDelegationSetDelegationTarget(), sender);
            case DELEGATION_ADDED:
                return DelegationAdded.parse(delegationEvent.getDelegationAdded(), sender);
            case DELEGATION_REMOVED:
                return DelegationRemoved.parse(delegationEvent.getDelegationRemoved(), sender);
            default:
                throw new IllegalArgumentException();
        }
    }
}
