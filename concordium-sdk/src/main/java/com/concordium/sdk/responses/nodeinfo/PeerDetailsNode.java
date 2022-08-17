package com.concordium.sdk.responses.nodeinfo;

import concordium.ConcordiumP2PRpc;
import lombok.Getter;
import lombok.val;

/**
 * Represents PeerDetail of type {@link PeerType#NODE}.
 */
@Getter
public class PeerDetailsNode extends PeerDetails {

    private final ConsensusState consensusState;

    PeerDetailsNode(ConsensusState consensusState) {
        super(PeerType.NODE);
        this.consensusState = consensusState;
    }

    /**
     * Parses input {@link NodeInfo} to {@link PeerDetails}.
     * @param value type of {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link PeerDetails}.
     */
    public static PeerDetails parse(ConcordiumP2PRpc.NodeInfoResponse value) {
        val consensusState = parseConsensusState(value);

        if(consensusState == ConsensusState.ACTIVE) {
            return PeerDetailsNodeActive.parse(value);
        }

        return new PeerDetailsNode(consensusState);
    }

    /**
     * Parses {@link ConsensusState} from input {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @param value input {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link ConsensusState}.
     */
    private static ConsensusState parseConsensusState(ConcordiumP2PRpc.NodeInfoResponse value) {
        if(!value.getConsensusRunning()) {
            return ConsensusState.NOT_RUNNING;
        }

        if(!value.getConsensusBakerRunning()) {
            return ConsensusState.PASSIVE;
        }

        return ConsensusState.ACTIVE;
    }
}
