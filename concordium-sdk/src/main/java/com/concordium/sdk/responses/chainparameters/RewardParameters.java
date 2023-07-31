package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
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
    RewardParameters(@JsonProperty("mintDistribution") JsonNode mintDistribution,
                     @JsonProperty("transactionFeeDistribution") TransactionFeeDistribution transactionFeeDistribution,
                     @JsonProperty("gASRewards") GasRewards gasRewards) {
        if (!mintDistribution.findPath("mintPerSlot").isMissingNode()) {
            this.mintDistribution = JsonMapper.INSTANCE.convertValue(mintDistribution, MintDistributionCpV0.class);
        } else {
            this.mintDistribution = JsonMapper.INSTANCE.convertValue(mintDistribution, MintDistributionCpV1.class);
        }
        this.transactionFeeDistribution = transactionFeeDistribution;
        this.gasRewards = gasRewards;
    }
}


