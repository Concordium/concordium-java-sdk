package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Additional information about a baking pool. This information is available from P4.
 */
@Data
@Jacksonized
@Builder
public class PoolInfo {

    /**
     * The status of whether a baking pool allows delegators to join.
     */
    private final OpenStatus openStatus;

    /**
     * The URL that links to the metadata about the pool.
     */
    private final String metadataUrl;

    /**
     * The commission rates charged by the pool owner.
     */
    private final CommissionRates commissionRates;
}
