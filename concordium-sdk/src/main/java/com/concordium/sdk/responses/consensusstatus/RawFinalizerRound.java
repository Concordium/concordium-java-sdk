package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.FinalizerIndex;
import com.concordium.sdk.responses.Round;
import com.google.common.collect.ImmutableList;
import lombok.*;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RawFinalizerRound {

    /**
     * The round that was signed off.
     */
    private final Round round;

    /**
     * The finalizers (identified by their 'FinalizerIndex') that
     * signed off in 'round'.
     */
    @Singular
    private final ImmutableList<FinalizerIndex> finalizers;
}
