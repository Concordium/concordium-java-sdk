package com.concordium.sdk.responses.chainparameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Distribution of gas rewards for chain parameters v0 and v1.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class GasRewards {

    /**
     * Fraction paid for including an update transaction in a block.
     */
    @JsonProperty("chainUpdate")
    private final double chainUpdate;

    /**
     * Fraction paid for including each account creation transaction in a block.
     */
    @JsonProperty("accountCreation")
    private final double accountCreation;

    /**
     *  The fraction paid to the baker.
     */
    @JsonProperty("baker")
    private final double baker;

    /**
     * Fraction paid for including a finalization proof in a block.
     */
    @JsonProperty("finalizationProof")
    private final double finalizationProof;
}
