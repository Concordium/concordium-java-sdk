package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * If the account is delegating.
 */
@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
@Builder
public final class AccountDelegation {
    /**
     * Whether earnings should be restaked.
     */
    private final boolean restakeEarnings;
    /**
     * The {@link DelegationTarget} that the account delegates to.
     */
    private final DelegationTarget target;
    /**
     * The staked amount for this account.
     */
    private final CCDAmount stakedAmount;

    /**
     * The pending changes for the delegator.
     */
    private final PendingChange pendingChange;
}
