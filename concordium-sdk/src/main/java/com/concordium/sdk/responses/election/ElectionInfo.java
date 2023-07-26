package com.concordium.sdk.responses.election;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.Optional;

/**
 * Contains information related to baker election for a particular block.
 */
@Builder
@ToString
@EqualsAndHashCode
public class ElectionInfo {
    /**
     * Baking lottery election difficulty.
     * Note that this is only set for protocol versions 1-5.
     */
    private final Double electionDifficulty;

    /**
     * Current leadership election nonce for the lottery.
     */
    @Getter
    private final byte[] leadershipElectionNonce;

    /**
     * List of the currently eligible bakers.
     */
    @Getter
    private final ImmutableList<ElectionInfoBaker> bakerElectionInfo;

    /**
     * Get the election difficulty if present.
     * This is present in protocols 1-5.
     * @return The election difficulty
     */
    public Optional<Double> getElectionDifficulty() {
        return Optional.ofNullable(this.electionDifficulty);
    }
}
