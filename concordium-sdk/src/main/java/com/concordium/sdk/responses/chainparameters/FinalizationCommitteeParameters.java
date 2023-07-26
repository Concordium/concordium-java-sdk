package com.concordium.sdk.responses.chainparameters;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Finalization committee parameters for {@link ChainParametersV2}
 */
@EqualsAndHashCode
@Getter
@ToString
@Builder
public class FinalizationCommitteeParameters {

    /**
     * The minimum number of finalizers in the finalization committee.
     */
    private final int minimumFinalizers;

    /**
     * The maximum number of finalizers in the finalization committee.
     */
    private final int maxFinalizers;

    /**
     * The threshold for determining the stake required for being eligible the finalization committee.
     * The amount is given by `total stake in pools * finalizer_relative_stake_threshold`
     */
    private final double finalizerRelativeStakeThreshold;

}
