package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.grpc.v2.NodeInfo;
import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Node {

    private ConsensusState consensusState;

    /**
     * Null if consensusState isn't ACTIVE
     */
    private BakerConsensusInfo bakerInfo;

    //TODO comment also for helper methods
    public static Node parseNodeInfo(NodeInfo nodeInfo) {

        // Node is a BOOTSTRAPPER
        if (!nodeInfo.hasNode()) {return buildBootstrapperNode();}

        // Node runs consensus but is not baker
        if (!nodeInfo.getNode().hasActive()) {return buildPassiveNode();}

        // Node is active baker
        return buildActiveNode(nodeInfo);


    }

    private static Node buildBootstrapperNode() {
        return Node.builder()
                .consensusState(ConsensusState.NOT_RUNNING)
                .bakerInfo(null)
                .build();
    }

    private static Node buildPassiveNode() {
        return Node.builder()
                .consensusState(ConsensusState.PASSIVE)
                .bakerInfo(null)
                .build();
    }

    private static Node buildActiveNode(NodeInfo nodeInfo) {
        return Node.builder()
                .consensusState(ConsensusState.ACTIVE)
                .bakerInfo(BakerConsensusInfo.parse(nodeInfo.getNode().getActive()))
                .build();
    }
}
