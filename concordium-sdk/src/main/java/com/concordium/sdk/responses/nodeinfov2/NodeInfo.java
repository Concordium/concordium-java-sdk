package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;

import static com.concordium.sdk.ClientV2MapperExtensions.toDuration;
import static com.concordium.sdk.ClientV2MapperExtensions.toZonedDateTime;

@EqualsAndHashCode
@Builder
public class NodeInfo {

    /**
     * The version of the node
     */
    @Getter
    private final String peerVersion;

    /**
     * Local time of the node
     */
    @Getter
    private final ZonedDateTime localTime;

    /**
     * Duration the node has been alive
     */
    @Getter
    private final Duration peerUptime;

    /**
     * Information related to the p2p protocol
     */
    @Getter
    private final NetworkInfo networkInfo;

    private final Node node;

    /**
     * Type of Peer. Bootstrapper or Node
     */
    @Getter
    private final PeerType peerType;

    /**
     * Gets the Consensus info for a node configured with baker keys
     * {@link ConsensusState} of the node must be {@link ConsensusState#ACTIVE}
     * @return {@link BakerConsensusInfo} of node configured with baker keys
     */
    public BakerConsensusInfo getBakerInfo() {
        if (!this.getConsensusStatus().equals(ConsensusState.ACTIVE)) {
            throw new IllegalStateException("Node is not ACTIVE but was: " + this.getConsensusStatus());
        }
        return node.getBakerInfo();
    }

    /**
     * Gets the Consensus Status of the node.
     * @return {@link ConsensusState} of the node.
     */
    public ConsensusState getConsensusStatus() {
        return node.getConsensusState();
    }

    /**
     * Parses {@link com.concordium.grpc.v2.NodeInfo.Node} to {@link NodeInfo}
     *
     * @param nodeInfo {@link  com.concordium.grpc.v2.NodeInfo} returned from the Grpc API
     * @return Parsed {@link NodeInfo}
     */
    public static NodeInfo parse(com.concordium.grpc.v2.NodeInfo nodeInfo) {
        return getStandardNodeInfoBuilder(nodeInfo)
                .node(Node.parseNodeInfo(nodeInfo))
                .build();

    }


    /**
     * Helper method for parse method. Creates a {@link NodeInfoBuilder} and sets fields that are always available
     *
     * @param nodeInfo {@link com.concordium.grpc.v2.NodeInfo} returned from the Grpc API
     * @return {@link NodeInfoBuilder} with peerVersion, localTime, peerUptime, networkInfo and peerType configured
     */
    private static NodeInfoBuilder getStandardNodeInfoBuilder(com.concordium.grpc.v2.NodeInfo nodeInfo) {
        return NodeInfo.builder()
                .peerVersion(nodeInfo.getPeerVersion())
                .localTime(toZonedDateTime(nodeInfo.getLocalTime()))
                .peerUptime(toDuration(nodeInfo.getPeerUptime()))
                .networkInfo(NetworkInfo.parse(nodeInfo.getNetworkInfo()))
                .peerType((nodeInfo.hasNode() ? PeerType.NODE : PeerType.BOOTSTRAPPER));
    }


}
