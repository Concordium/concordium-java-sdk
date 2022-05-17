package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * If the account is delegating.
 */
@Getter
@ToString
@EqualsAndHashCode
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

    @JsonCreator
    AccountDelegation(@JsonProperty("restakeEarnings") boolean restakeEarnings,
                      @JsonProperty("delegationTarget") DelegationTarget target,
                      @JsonProperty("stakedAmount") CCDAmount stakedAmount) {
        this.restakeEarnings = restakeEarnings;
        this.target = target;
        this.stakedAmount = stakedAmount;
    }
}
