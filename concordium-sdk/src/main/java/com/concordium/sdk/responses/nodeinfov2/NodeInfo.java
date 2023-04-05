package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.sdk.responses.nodeinfo.BakingStatus;
import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;

import static com.concordium.sdk.ClientV2MapperExtensions.toZonedDateTime;

@EqualsAndHashCode
@Builder
public class NodeInfo {

    @Getter
    private final String peerVersion;

    @Getter
    private final ZonedDateTime localTime;

    @Getter
    private final Duration peerUptime;

    @Getter
    private final NetworkInfo networkInfo;

    private final Node node;

    @Getter
    private final PeerType peerType;

    //TODO error message and comment
    public BakerConsensusInfo getBakerInfo() {
        if (this.peerType.equals(PeerType.BOOTSTRAPPER)) {
            throw new IllegalStateException("Not allowed");
        }
        if (!this.getConsensusStatus().equals(ConsensusState.ACTIVE)) {
            throw new IllegalStateException("Not allowed");
        }
        return node.getBakerInfo();
    }

    //TODO eror message and comment
    public ConsensusState getConsensusStatus() {
        if (this.peerType.equals(PeerType.BOOTSTRAPPER)) {
            throw new IllegalStateException("Not allowed");
        }
        return node.getConsensusState();
    }

    //TODO comment
    public static NodeInfo parse(com.concordium.grpc.v2.NodeInfo nodeInfo) {

        var builder = getStandardNodeInfoBuilder(nodeInfo);

        // Node is a BOOTSTRAPPER
        if (!nodeInfo.hasNode()) {
            return NodeInfo.getNodeInfoForBootstrapper(builder, nodeInfo);
        }

        builder = builder.peerType(PeerType.NODE);

        // Node is not configured with baker keys
        if (!nodeInfo.getNode().hasActive()) {
            return getNodeInfoForPassive(builder);
        }

        builder = builder.consensusState(ConsensusState.ACTIVE);


        return builder.build();
    }


    //TODO comment
    private static NodeInfoBuilder getStandardNodeInfoBuilder(com.concordium.grpc.v2.NodeInfo nodeInfo) {
        return NodeInfo.builder()
                .peerVersion(nodeInfo.getPeerVersion())
                .localTime(toZonedDateTime(nodeInfo.getLocalTime()))
                .peerUptime(Duration.ofMillis(nodeInfo.getPeerUptime().getValue()))
                .networkInfo(NetworkInfo.parse(nodeInfo.getNetworkInfo()));
    }

    //TODO comment
    private static NodeInfo getNodeInfoForBootstrapper(NodeInfoBuilder builder, com.concordium.grpc.v2.NodeInfo nodeInfo) {

        val node = Node.builder()
                .consensusState(ConsensusState.NOT_RUNNING)
                .bakerInfo(null).build();
        return builder
                .node(node)
                .peerType(PeerType.BOOTSTRAPPER).build();

    }


}
