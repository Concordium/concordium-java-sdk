package com.concordium.sdk.responses.chainparameters;

/**
 * A common class for {@link MintDistributionCpV0} and {@link MintDistributionCpV1}
 * Check with {@link MintDistribution#getType()} before casting.
 */
public abstract class MintDistribution {
    public abstract MintDistributionType getType();

    public enum MintDistributionType {
        V1, V2
    }
}
