package com.concordium.sdk.responses.peerStats;

import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PeerStat {

    /**
     * Peer Node Id. The node id is an unsigned 64 bit integer encoded as a hex string.
     */
    private final String nodeId;

    /**
     * Latency to the peer.
     */
    private final long latency;

    /**
     * Total packets received from the peer.
     */
    private final long packetsReceived;

    /**
     * Total packets sent to the peer.
     */
    private final long packetsSent;

    /**
     * Parses the Grpc response to {@link PeerStat}
     *
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
