package com.concordium.sdk.responses.nodeinfo;

import com.concordium.grpc.v2.NodeInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Node {

    /**
     * Consensus Status of the node
     */
    private ConsensusState consensusState;

    /**
     * Consensus info for a node configured with baker keys
     * Null if {@link ConsensusState} of the node is not {@link ConsensusState#ACTIVE}
     */
    private BakerConsensusInfo bakerInfo;

    /**
     * Parses {@link NodeInfo} to {@link Node}
     *
     * @param nodeInfo {@link NodeInfo} returned from the Grpc API
     * @return Parsed {@link Node}
     */
    public static Node parseNodeInfo(NodeInfo nodeInfo) {

        // Node is a BOOTSTRAPPER
        if (!nodeInfo.hasNode()) {
            return buildBootstrapperNode();
        }

        // Node runs consensus but is not baker
        if (!nodeInfo.getNode().hasActive()) {
            return buildPassiveNode();
        }

        // Node is active baker
        return buildActiveNode(nodeInfo);


    }

    /**
     * Helper method for building BOOTSTRAPPER node
     *
     * @return {@link Node} not running consensus
     */
    private static Node buildBootstrapperNode() {
        return Node.builder()
                .consensusState(ConsensusState.NOT_RUNNING)
                .bakerInfo(null)
                .build();
    }

    /**
     * Helper method for building node not configured with baker keys
     *
     * @return {@link Node} not configured with baker keys
     */
    private static Node buildPassiveNode() {
        return Node.builder()
                .consensusState(ConsensusState.PASSIVE)
                .bakerInfo(null)
                .build();
    }

    /**
     * Helper method for building node configured with baker keys
     *
     * @param nodeInfo {@link NodeInfo} returned from the Grpc API
     * @return {@link Node} configured with baker keys
     */
    private static Node buildActiveNode(NodeInfo nodeInfo) {
        return Node.builder()
                .consensusState(ConsensusState.ACTIVE)
                .bakerInfo(BakerConsensusInfo.parse(nodeInfo.getNode().getActive()))
                .build();
    }
}
