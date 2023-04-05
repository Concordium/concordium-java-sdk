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

    private final String nodeID;
    private final UInt64 peerTotalSent;
    private final UInt64 peerTotalRecieved;
    private final UInt64 avgBpsIn;
    private final UInt64 avgBpsOut;

    /**
     * Parses {@link com.concordium.grpc.v2.NodeInfo.NetworkInfo} to {@link NetworkInfo}
     * @param networkInfo {@link com.concordium.grpc.v2.NodeInfo.NetworkInfo} returned from the Grpc API
     * @return Parsed {@link NetworkInfo}
     */
    public static NetworkInfo parse(NodeInfo.NetworkInfo networkInfo) {
        return NetworkInfo.builder().nodeID(networkInfo.getNodeId().getValue())
                .peerTotalSent(UInt64.from(networkInfo.getPeerTotalSent()))
                .peerTotalRecieved(UInt64.from(networkInfo.getPeerTotalReceived()))
                .avgBpsIn(UInt64.from(networkInfo.getAvgBpsIn()))
                .avgBpsOut(UInt64.from(networkInfo.getAvgBpsOut())).build();
    }
}
