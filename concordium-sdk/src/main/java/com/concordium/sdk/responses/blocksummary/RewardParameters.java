package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class RewardParameters {
    private final MintDistributionUpdates mintDistributionUpdates;
    private final TransactionFeeDistributionUpdates transactionFeeDistributionUpdates;
    private final GasRewards gasRewards;

    @JsonCreator
    RewardParameters(@JsonProperty("mintDistribution") MintDistributionUpdates mintDistributionUpdates,
                     @JsonProperty("transactionFeeDistribution") TransactionFeeDistributionUpdates transactionFeeDistributionUpdates,
                     @JsonProperty("gASRewards") GasRewards gasRewards) {
        this.mintDistributionUpdates = mintDistributionUpdates;
        this.transactionFeeDistributionUpdates = transactionFeeDistributionUpdates;
        this.gasRewards = gasRewards;
    }
}
