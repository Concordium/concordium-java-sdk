package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Baker pool info
 */
@ToString
@Getter
@EqualsAndHashCode
public class BakerPoolInfo {
    /**
     * The URL that links to the metadata about the pool.
     */
    private final String metadataUrl;
    /**
     * Whether the pool allows delegators.
     */
    private final OpenStatus openStatus;
    /**
     * The commission rates charged by the pool owner.
     */
    private final CommissionRates commissionRates;

    @JsonCreator
    BakerPoolInfo(@JsonProperty("metadataUrl") String metadataUrl,
                  @JsonProperty("openStatus") OpenStatus openStatus,
                  @JsonProperty("commissionRates") CommissionRates commissionRates) {
        this.metadataUrl = metadataUrl;
        this.openStatus = openStatus;
        this.commissionRates = commissionRates;
    }
}
