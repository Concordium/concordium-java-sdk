package com.concordium.sdk.responses.poolstatus;

import com.concordium.sdk.responses.accountinfo.CommissionRates;

/**
 * Additional information about a baking pool. This information is added with the introduction of delegation.
 */
public class PoolInfo {

    /**
     * The status of whether a baking pool allows delegators to join.
     */
    public BakerPoolOpenStatus openStatus;

    /**
     * The URL that links to the metadata about the pool.
     */
    public String metadataUrl;

    /**
     * The commission rates charged by the pool owner.
     */
    public CommissionRates commissionRates;
}
