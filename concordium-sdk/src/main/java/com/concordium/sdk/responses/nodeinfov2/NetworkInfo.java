package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.grpc.v2.NodeInfo;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * Network info attached to NodeInfo object
 */
@Builder
@Getter
@EqualsAndHashCode
public class NetworkInfo {

    /**
     * The node id
     */
    private final String nodeID;

    /**
     * Total number of packets sent by the node
     */
    private final UInt64 peerTotalSent;

    /**
     * Total number of packets received by the node
     */
    private final UInt64 peerTotalReceived;

    /**
     * Average outbound throughput in bytes per second
     */
    private final UInt64 avgBpsIn;

    /**
     * Average inbound throughput in bytes per second
     */
    private final UInt64 avgBpsOut;

    /**
     * Parses {@link com.concordium.grpc.v2.NodeInfo.NetworkInfo} to {@link NetworkInfo}
     * @param networkInfo {@link com.concordium.grpc.v2.NodeInfo.NetworkInfo} returned from the Grpc API
     * @return Parsed {@link NetworkInfo}
     */
    public static NetworkInfo parse(NodeInfo.NetworkInfo networkInfo) {
        return NetworkInfo.builder().nodeID(networkInfo.getNodeId().getValue())
                .peerTotalSent(UInt64.from(networkInfo.getPeerTotalSent()))
                .peerTotalReceived(UInt64.from(networkInfo.getPeerTotalReceived()))
                .avgBpsIn(UInt64.from(networkInfo.getAvgBpsIn()))
                .avgBpsOut(UInt64.from(networkInfo.getAvgBpsOut())).build();
    }
}
