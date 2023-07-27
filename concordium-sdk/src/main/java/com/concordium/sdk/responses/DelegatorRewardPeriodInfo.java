package com.concordium.sdk.responses;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
public class DelegatorRewardPeriodInfo {
    /**
     * The delegator account address.
     */
    private final AccountAddress account;

    /**
     * The amount of stake currently staked to the pool.
     */
    private final CCDAmount stake;
}
