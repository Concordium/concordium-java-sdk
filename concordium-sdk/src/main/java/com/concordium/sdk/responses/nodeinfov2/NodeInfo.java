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

    //TODO error message and comment
    public ConsensusState getConsensusStatus() {
        if (this.peerType.equals(PeerType.BOOTSTRAPPER)) {
            throw new IllegalStateException("Not allowed");
        }
        return node.getConsensusState();
    }

    //TODO comment
    public static NodeInfo parse(com.concordium.grpc.v2.NodeInfo nodeInfo) {
        return getStandardNodeInfoBuilder(nodeInfo)
                .node(Node.parseNodeInfo(nodeInfo))
                .build();

    }


    //TODO comment
    private static NodeInfoBuilder getStandardNodeInfoBuilder(com.concordium.grpc.v2.NodeInfo nodeInfo) {
        return NodeInfo.builder()
                .peerVersion(nodeInfo.getPeerVersion())
                .localTime(toZonedDateTime(nodeInfo.getLocalTime()))
                .peerUptime(toDuration(nodeInfo.getPeerUptime()))
                .networkInfo(NetworkInfo.parse(nodeInfo.getNetworkInfo()))
                .peerType((nodeInfo.hasNode() ? PeerType.NODE : PeerType.BOOTSTRAPPER));
    }




}
