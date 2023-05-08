package com.concordium.sdk.responses.peerlist;

import com.concordium.grpc.v2.PeersInfo;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Network statistics for a Peer
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class NetworkStats {
    /**
     * The number of messages sent to the peer.
     * Packets are blocks, transactions, catchup messages, finalization records
     * and network messages such as pings and peer requests
     */
    private UInt64 packetsSent;
    /**
     * The number of messages received from the peer
     * Packets are blocks, transactions, catchup messages, finalization records
     * and network messages such as pings and peer requests
     */
    private  UInt64 packetsReceived;
    /**
     * The connection latenct (i.e., ping time) in milliseconds
     */
    private UInt64 latency;

    /**
     * Parses {@link com.concordium.grpc.v2.PeersInfo.Peer.NetworkStats} to {@link NetworkStats}
     * @param networkStats {@link com.concordium.grpc.v2.PeersInfo.Peer.NetworkStats} returned from the Grpc API
     * @return Parsed {@link NetworkStats}
     */
    public static NetworkStats parse(PeersInfo.Peer.NetworkStats networkStats) {
        return NetworkStats.builder()
                .packetsSent(UInt64.from(networkStats.getPacketsSent()))
                .packetsReceived(UInt64.from(networkStats.getPacketsReceived()))
                .latency(UInt64.from(networkStats.getLatency()))
                .build();
    }
}
