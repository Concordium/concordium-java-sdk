package com.concordium.sdk.responses.peerStats;

import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.util.stream.Collectors;

@Builder
@Getter
@ToString
public class PeerStatistics {

    /**
     * Average bytes per second coming in to the node.
     */
    private final long averageBPSIn;

    /**
     * Average bytes per second going out of the node.
     */
    private final long averageBPSOut;

    /**
     * List of peers that the node is connected to containing their statistics.
     */
    private final ImmutableList<PeerStat> peerStats;

    public static PeerStatistics parse(ConcordiumP2PRpc.PeerStatsResponse value) {
        val peerStats = value
                .getPeerstatsList()
                .stream()
                .map(s->PeerStat.parse(s))
                .collect(Collectors.toList());

        return PeerStatistics.builder()
                .averageBPSIn(value.getAvgBpsIn())
                .averageBPSOut(value.getAvgBpsOut())
                .peerStats(ImmutableList.<PeerStat>builder().addAll(peerStats).build())
                .build();
    }
}
