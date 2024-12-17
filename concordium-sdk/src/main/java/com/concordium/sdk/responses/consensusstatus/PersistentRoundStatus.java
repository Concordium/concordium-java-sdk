package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Round;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class PersistentRoundStatus {

    /**
     * The last signed quorum message by the node.
     */
    private final QuorumMessage lastSignedQuorumMessage;

    /**
     * The last signed timeout message by the node.
     */
    private final TimeoutMessage lastSignedTimeoutMessage;

    /**
     * The last round the node baked in.
     */
    private final Round lastBakedRound;

    /**
     * The latest timeout certificate seen by the node. May be absent if the node has seen a
     * quorum certificate for a more recent round.
     */
    private final RawTimeoutCertificate latestTimeout;

    public Optional<RawTimeoutCertificate> getLatestTimeout() {
        return Optional.ofNullable(latestTimeout);
    }
}
