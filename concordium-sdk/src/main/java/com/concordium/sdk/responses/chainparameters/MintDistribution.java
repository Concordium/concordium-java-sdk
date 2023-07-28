package com.concordium.sdk.responses.chainparameters;

public abstract class MintDistribution {
    public abstract MintDistributionType getType();

    public enum MintDistributionType {
        V1, V2
    }
}
