package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Baker {
    private final int bakerId;
    private final String stakedAmount;
    private final boolean restakeEarnings;
    private final String bakerElectionVerifyKey;
    private final String bakerSignatureVerifyKey;
    private final String bakerAggregationVerifyKey;

    @JsonCreator
    Baker(@JsonProperty("bakerId") int bakerId,
          @JsonProperty("stakedAmount") String stakedAmount,
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
