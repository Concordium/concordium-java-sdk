package com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Parameters determining the distribution of transaction fees.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public class TransactionFeeDistributionV2 {

    /**
     *  The fraction allocated to the GAS account.
     */
    private final double gasAccount;
    /**
     * The fraction allocated to the baker.
     */
    private final double baker;
}
