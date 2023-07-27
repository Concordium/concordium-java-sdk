package com.concordium.sdk.responses.nodeinfo;

import com.concordium.sdk.types.Timestamp;
import concordium.ConcordiumP2PRpc;
import lombok.*;

/**
 * Node Information.
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class NodeInfo {
    /**
     * Node Id.
     */
    private final String nodeId;

    /**
     * Current local time of the node.
     */
    private final Timestamp localTime;

    /**
     * Type of Peer. Bootstrapper Or Node.
     */
    private final PeerType peerType;

    /**
     * Type of Peer Consensus Status.
     */
    private final ConsensusState consensusState;

    /**
     * Type of Peer Consensus Status in Baking Committee.
     */
    private final BakingStatus bakingStatus;

    /**
     * Committee details. This will only be populated if {@link NodeInfo#bakingStatus}
     * is {@link BakingStatus#ACTIVE_IN_COMMITTEE}.
     */
    private final BakingCommitteeDetails committeeDetails;

    /**
     * Parses {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} to {@link NodeInfo}.
     *
     * @param value {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link NodeInfo}.
     */
    public static NodeInfo parse(ConcordiumP2PRpc.NodeInfoResponse value) {
        val builder = NodeInfo.builder()
                .nodeId(value.getNodeId().getValue())
                .localTime(Timestamp.newMillis(value.getCurrentLocaltime()))
                .peerType(parsePeerType(value))
                .consensusState(parseConsensusState(value))
                .bakingStatus(parseBakingStatus(value))
                .committeeDetails(BakingCommitteeDetails.parse(value));

        return builder.build();
    }

    private static PeerType parsePeerType(ConcordiumP2PRpc.NodeInfoResponse value) {
        switch (value.getPeerType()) {
            case "Bootstrapper":
                return PeerType.BOOTSTRAPPER;
            case "Node":
                return PeerType.NODE;
            default:
                throw new IllegalArgumentException(
                        String.format("Invalid 'PeerType': %s", value.getPeerType()));
        }
    }

    private static BakingStatus parseBakingStatus(ConcordiumP2PRpc.NodeInfoResponse value) {
        switch (value.getConsensusBakerCommittee()) {
            case ACTIVE_IN_COMMITTEE:
                return BakingStatus.ACTIVE_IN_COMMITTEE;
            case NOT_IN_COMMITTEE:
                return BakingStatus.NOT_IN_COMMITTEE;
            case ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE:
                return BakingStatus.ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE;
            case ADDED_BUT_WRONG_KEYS:
                return BakingStatus.ADDED_BUT_WRONG_KEYS;
            default:
            case UNRECOGNIZED:
                throw new IllegalArgumentException(String.format(
                        "Invalid value of Consensus Baker Committee : %d",
                        value.getConsensusBakerCommittee().getNumber()));
        }
    }

    /**
     * Parses {@link ConsensusState} from input {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     *
     * @param value input {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link ConsensusState}.
     */
    private static ConsensusState parseConsensusState(ConcordiumP2PRpc.NodeInfoResponse value) {
        if (!value.getConsensusRunning()) {
            return ConsensusState.NOT_RUNNING;
        }

        if (!value.getConsensusBakerRunning()) {
            return ConsensusState.PASSIVE;
        }

        return ConsensusState.ACTIVE;
    }

}
