package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    @JsonProperty("restakeEarnings")
    private final boolean restakeEarnings;
    /**
     * The {@link DelegationTarget} that the account delegates to.
     */
    @JsonProperty("delegationTarget")
    private final DelegationTarget target;
    /**
     * The staked amount for this account.
     */
    @JsonProperty("stakedAmount")
    private final CCDAmount stakedAmount;

    /**
     * The pending changes for the delegator.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "change")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ReduceStakeChange.class, name = "ReduceStake"),
            @JsonSubTypes.Type(value = RemoveStakeChange.class, name = "RemoveStake")
    })
    @JsonProperty("pendingChange")
    private final PendingChange pendingChange;
}
