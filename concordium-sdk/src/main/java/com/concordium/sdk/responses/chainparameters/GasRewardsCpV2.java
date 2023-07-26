package com.concordium.sdk.responses.chainparameters;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class GasRewardsCpV2 {

    /**
     * Fraction paid for including an update transaction in a block.
     */
    private final double chainUpdate;

    /**
     * Fraction paid for including each account creation transaction in a block.
     */
    private final double accountCreation;

    /**
     * The fraction paid to the baker.
     */
    private final double baker;
}
