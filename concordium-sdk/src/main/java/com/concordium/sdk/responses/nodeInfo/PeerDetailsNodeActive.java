package com.concordium.sdk.responses.nodeInfo;

import concordium.ConcordiumP2PRpc;
import lombok.Builder;

import java.util.Optional;

/**
 * Represents {@link PeerDetailsNode} of type {@link ConsensusState.Active}.
 */
public class PeerDetailsNodeActive extends PeerDetailsNode {

    /**
     * Type of State of the Active Node.
     */
    private final NodeActiveStateType activeStateType;

    /**
     * Committee details. This will only be populated if activeStateType is ActiveInCommittee.
     */
    private final Optional<CommitteeDetails> committeeDetails;

    @Builder
    private PeerDetailsNodeActive(NodeActiveStateType activeStateType, Optional<CommitteeDetails> committeeDetails)
    {
        super(ConsensusState.Active);
        this.activeStateType = activeStateType;
        this.committeeDetails = committeeDetails;
    }

    /**
     * Parses input {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} to {@link PeerDetailsNodeActive}.
     * @param value Input {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link PeerDetailsNodeActive}.
     */
    public static PeerDetailsNodeActive parse(ConcordiumP2PRpc.NodeInfoResponse value) {
        switch(value.getConsensusBakerCommittee()) {
            case ACTIVE_IN_COMMITTEE:
                return PeerDetailsNodeActive.builder()
                        .committeeDetails(Optional.of(CommitteeDetails.parse(value)))
                        .activeStateType(NodeActiveStateType.ActiveInCommittee)
                        .build();
            case NOT_IN_COMMITTEE:
                return PeerDetailsNodeActive.builder()
                        .committeeDetails(Optional.empty())
                        .activeStateType(NodeActiveStateType.NotInCommittee)
                        .build();
            case ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE:
                return PeerDetailsNodeActive.builder()
                        .committeeDetails(Optional.empty())
                        .activeStateType(NodeActiveStateType.AddedButNotActiveInCommittee)
                        .build();
            case ADDED_BUT_WRONG_KEYS:
                return PeerDetailsNodeActive.builder()
                        .committeeDetails(Optional.empty())
                        .activeStateType(NodeActiveStateType.AddedButWrongKeys)
                        .build();
            default:
            case UNRECOGNIZED:
                return PeerDetailsNodeActive.builder()
                        .committeeDetails(Optional.empty())
                        .activeStateType(NodeActiveStateType.Unrecognized)
                        .build();
        }
    }
}
