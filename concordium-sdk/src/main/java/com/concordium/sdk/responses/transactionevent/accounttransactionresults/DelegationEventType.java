package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.sdk.responses.transactionstatus.*;

/**
 * Delegation event types used with the GRPCv2 API.
 */
public enum DelegationEventType {

    /**
     * The delegator increased its stake.
     * This event type corresponds to the concrete event {@link DelegationStakeIncreased}.
     */
    DELEGATION_STAKE_INCREASED,
    /**
     * The delegator decreased its stake.
     * This event type corresponds to the concrete event {@link DelegationStakeDecreased}.
     */
    DELEGATION_STAKE_DECREASED,
    /**
     * The delegator set its restake property.
     * This event type corresponds to the concrete event {@link DelegationSetRestakeEarnings}.
     */
    DELEGATION_SET_RESTAKE_EARNINGS,
    /**
     * The delegator set a target for delegation.
     * This event type corresponds to the concrete event {@link DelegationSetDelegationTarget}.
     */
    DELEGATION_SET_DELEGATION_TARGET,
    /**
     * The sender of the transaction has started delegating.
     * This event type corresponds to the concrete event {@link DelegationAdded}.
     */
    DELEGATION_ADDED,
    /**
     * Delegator stopped its delegation.
     * This event type corresponds to the concrete event {@link DelegationRemoved}.
     */
    DELEGATION_REMOVED
}


