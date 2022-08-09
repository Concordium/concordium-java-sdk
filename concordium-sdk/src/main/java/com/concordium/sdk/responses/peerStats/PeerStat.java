package com.concordium.sdk.responses.peerStats;

import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PeerStat {

    /**
     * Peer Node Id. Serialized has hex string.
     */
    private final String nodeId;

    /**
     * Latency to the peer.
     */
    private final long latency;

    /**
     * Total packets received from the Peer.
     */
    private final long packetsReceived;

    /**
     * Total packets sent to the Peer.
     */
    private final long packetsSent;

    /**
     * Parses the Grpc response to {@link PeerStat}
     * @param stat of type {@link ConcordiumP2PRpc.PeerStatsResponse.PeerStats}
     * @return Parsed {@link PeerStat}
     */
    public static PeerStat parse(ConcordiumP2PRpc.PeerStatsResponse.PeerStats stat) {
        return PeerStat.builder()
                .nodeId(stat.getNodeId())
                .latency(stat.getLatency())
                .packetsReceived(stat.getPacketsReceived())
                .packetsSent(stat.getPacketsSent())
                .build();
    }
}
