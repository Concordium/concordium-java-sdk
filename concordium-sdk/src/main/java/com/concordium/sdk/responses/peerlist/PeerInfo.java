package com.concordium.sdk.responses.peerlist;

import com.concordium.grpc.v2.PeersInfo;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import com.concordium.sdk.types.UInt16;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ToString
@EqualsAndHashCode
@Builder
@Getter
public class PeerInfo {

    /**
     * The self-chosen identifier of the peer.
     */
    private final String nodeId;
    /**
     * The port of the peer
     */
    private final UInt16 port;
    /**
     * The ip address of the peer
     */
    private final InetAddress ipAddress;
    /**
     * The catchup status of the peer
     */
    private final PeerCatchupStatus catchupStatus;

    /**
     * Network statistics of the Peer
     */
    private NetworkStats networkStats;

    /**
     * Type of the peer.
     */
    private PeerType peerType;

    /**
     * Parses {@link PeersInfo} to an {@link ImmutableList} of {@link PeerInfo}
     *
     * @param peersInfo {@link PeersInfo} returned by the Grpc API
     * @return an {@link ImmutableList} of {@link PeerInfo}
     */
    public static ImmutableList<PeerInfo> parseToList(PeersInfo peersInfo) throws UnknownHostException {
        val list = new ImmutableList.Builder<PeerInfo>();

        for (PeersInfo.Peer peer : peersInfo.getPeersList()) {
            list.add(parse(peer));
        }
        return list.build();
    }

    /**
     * Parses {@link com.concordium.grpc.v2.PeersInfo.Peer} to {@link PeerInfo}
     *
     * @param peer {@link com.concordium.grpc.v2.PeersInfo.Peer} from Grpc API
     * @return parsed {@link PeerInfo}
     */
    private static PeerInfo parse(PeersInfo.Peer peer) throws UnknownHostException {
        PeerCatchupStatus catchupStatus = (peer.hasNodeCatchupStatus() ? PeerCatchupStatus.parse(peer.getNodeCatchupStatus()) : PeerCatchupStatus.NOT_AVAILABLE);
        val peerType = (peer.hasNodeCatchupStatus() ? PeerType.NODE : PeerType.BOOTSTRAPPER);
        return PeerInfo.builder()
                .nodeId(peer.getPeerId().getValue())
                .port(UInt16.from(peer.getSocketAddress().getPort().getValue()))
                .ipAddress(InetAddress.getByName(peer.getSocketAddress().getIp().getValue()))
                .catchupStatus(catchupStatus)
                .networkStats(NetworkStats.parse(peer.getNetworkStats()))
                .peerType(peerType)
                .build();
    }

}
