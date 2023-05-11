package com.concordium.sdk.responses.election;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Contains information related to baker election for a particular block.
 */
@Builder
@ToString
@EqualsAndHashCode
public class ElectionInfo {
    /**
     * Baking lottery election difficulty.
     */
    private final double electionDifficulty;

    /**
     * Current leadership election nonce for the lottery.
     */
    private final byte[] leadershipElectionNonce;

    /**
     * List of the currently eligible bakers.
     */
    private final ImmutableList<ElectionInfoBaker> bakerElectionInfo;
}
