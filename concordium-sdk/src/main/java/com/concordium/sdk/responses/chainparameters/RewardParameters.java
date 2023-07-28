package com.concordium.sdk.responses.chainparameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Reward parameters returned as part of the chain parameters for
 * v1 grpc API.
 */
@Getter
@ToString
public final class RewardParameters {
    private final MintDistribution mintDistribution;
    private final TransactionFeeDistribution transactionFeeDistribution;
    private final GasRewards gasRewards;

    @JsonCreator
    RewardParameters(@JsonProperty("mintDistribution") MintDistribution mintDistribution,
                     @JsonProperty("transactionFeeDistribution") TransactionFeeDistribution transactionFeeDistribution,
                     @JsonProperty("gASRewards") GasRewards gasRewards) {
        this.mintDistribution = mintDistribution;

        this.transactionFeeDistribution = transactionFeeDistribution;
        this.gasRewards = gasRewards;
    }
}


