package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableList;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class BakersAndFinalizers {
    @Singular
    private ImmutableList<FullBakerInfo> bakers;

    /**
     * The IDs of the bakers that are finalizers.
     * The order determines the finalizer index.
     */
    @Singular
    private final ImmutableList<BakerId> finalizers;

    /**
     * The total effective stake of the bakers.
     */
    private final CCDAmount bakerTotalStake;

    /**
     * The total effective stake of the finalizers.
     */
    private final CCDAmount finalizerTotalStake;

    /**
     * The hash of the finalization committee.
     */
    private final Hash finalizationCommitteeHash;
}
