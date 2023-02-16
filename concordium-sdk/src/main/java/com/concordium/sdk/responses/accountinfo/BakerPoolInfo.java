package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Baker pool info
 */
@ToString
@Getter
@EqualsAndHashCode
@Jacksonized
@Builder
public class BakerPoolInfo {
    /**
     * The URL that links to the metadata about the pool.
     */
    @JsonProperty("metadataUrl")
    private final String metadataUrl;

    /**
     * Whether the pool allows delegators.
     */
    @JsonProperty("openStatus")
    private final OpenStatus openStatus;

    /**
     * The commission rates charged by the pool owner.
     */
    @JsonProperty("commissionRates")
    private final CommissionRates commissionRates;
}
