package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Round;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.util.Optional;

/**
 * The current round status.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RoundStatus {

    /**
     * The current round from the perspective of the node.
     * This should always be higher than the round of the highest certified block.
     * If the previous round did not timeout, it should be one more than the round of
     * the `highest_certified_block`. Otherwise, it should be one more than the round of
     * the `previous_round_timeout`.
     */
    private final Round currentRound;

    /**
     * The quorum certificate for the highest certified block.
     */
    private final RawQuorumCertificate highestCertifiedBlock;

    /**
     * If the last round timed out, this is the timeout certificate for that round and
     * the highest quorum certificate at the time the round timed out.
     */
    private final RoundTimeout previousRoundTimeout;

    public Optional<RoundTimeout> getPreviousRoundTimeout() {
        return Optional.ofNullable(previousRoundTimeout);
    }

    /**
     * Flag indicating whether the node should attempt to bake in the current round.
     * This is set to true when the round is advanced, and set to false once the node has
     * attempted to bake for the round.
     */
    private final boolean roundEligibleToBake;

    /**
     * The current epoch. This should either be the same as the epoch of the last finalized
     * block (if its timestamp is before the trigger block time) or the next epoch from the last
     * finalized block (if its timestamp is at least the trigger block time).
     */
    private final Epoch currentEpoch;

    /**
     * If present, an epoch finalization entry for the epoch before `current_epoch`.
     * An entry must be present if the current epoch is greater than the epoch of the last
     * finalized block.
     */
    private final RawFinalizationEntry lastEpochFinalizationEntry;

    /**
     * The current duration the node will wait before a round times out.
     */
    private final Duration currentTimeout;
}
