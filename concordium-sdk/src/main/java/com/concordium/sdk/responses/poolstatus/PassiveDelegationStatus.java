package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@EqualsAndHashCode(callSuper = false)
public class PassiveDelegationStatus extends PoolStatus {

    private final PoolType poolType = PoolType.PASSIVE_DELEGATION;

    /**
     * The total capital delegated passively.
     */
    private final CCDAmount delegatedCapital;

    /**
     * "The passive delegation commission rates.
     */
    private final CommissionRates commissionRates;

    /**
     * The transaction fees accruing to the passive delegators in the current reward period.
     */
    private final CCDAmount currentPaydayTransactionFeesEarned;

    /**
     * The effective delegated capital to the passive delegators for the current reward period.
     */
    private final CCDAmount currentPaydayDelegatedCapital;

    /**
     * Total capital staked across all pools, including passive delegation.
     */
    private final CCDAmount allPoolTotalCapital;
}
