package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.responses.TimeoutParameters;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Consensus parameters for chain parameters version 2.
 * Effective from protocol version 6 and onwards.
 */
@ToString
@EqualsAndHashCode
@Builder
@Getter
public class ConsensusParameters {
    /**
     * The current timeout parameters.
     * These parameters determines how the duration
     * of rounds grows and shrinks if timeouts occurs.
     */
    private final TimeoutParameters timeoutParameters;

    /**
     * The minimum block time.
     * Blocks will not be produced faster than the minimum block time.
     */
    private final java.time.Duration minBlockTime;

    /**
     * The maximum block energy limit.
     * This parameter determines how much energy can
     * be spent in a block.
     */
    private final UInt64 blockEnergyLimit;
}
