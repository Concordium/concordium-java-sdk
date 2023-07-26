package com.concordium.sdk.responses.chainparameters;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class MintDistributionCpV1 {
    /**
     * The fraction of newly created CCD allocated to baker rewards.
     */
    private final double bakingReward;

    /**
     * The fraction of newly created CCD allocated to finalization rewards.
     */
    private final double finalizationReward;
}