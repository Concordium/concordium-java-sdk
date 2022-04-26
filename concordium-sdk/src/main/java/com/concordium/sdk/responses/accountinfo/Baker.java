package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Baker {
    /**
     * The baker id.
     * Note. The baker id is non-negative.
     */
    private final int bakerId;
    /**
     * The staked amount.
     */
    private final CCDAmount stakedAmount;
    /**
     * Whether earnings should be restaked automatically or not.
     */
    private final boolean restakeEarnings;
    /**
     * The baker's public VRF key
     */
    private final String bakerElectionVerifyKey;
    /**
     * The baker's public signature key
     */
    private final String bakerSignatureVerifyKey;
    /**
     * The baker's public key for finalization record aggregation
     */
    private final String bakerAggregationVerifyKey;

    @JsonCreator
    Baker(@JsonProperty("bakerId") int bakerId,
          @JsonProperty("stakedAmount") CCDAmount stakedAmount,
          @JsonProperty("restakeEarnings") boolean restakeEarnings,
          @JsonProperty("bakerElectionVerifyKey") String bakerElectionVerifyKey,
          @JsonProperty("bakerSignatureVerifyKey") String bakerSignatureVerifyKey,
          @JsonProperty("bakerAggregationVerifyKey") String bakerAggregationVerifyKey) {
        this.bakerId = bakerId;
        this.stakedAmount = stakedAmount;
        this.restakeEarnings = restakeEarnings;
        this.bakerElectionVerifyKey = bakerElectionVerifyKey;
        this.bakerSignatureVerifyKey = bakerSignatureVerifyKey;
        this.bakerAggregationVerifyKey = bakerAggregationVerifyKey;
    }
}
