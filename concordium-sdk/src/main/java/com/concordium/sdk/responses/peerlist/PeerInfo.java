package com.concordium.sdk.responses.peerlist;

import com.concordium.grpc.v2.PeersInfo;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import com.concordium.sdk.types.UInt16;
import com.google.common.collect.ImmutableList;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PeerInfo extends Peer{

    /**
     * Network statistics of the Peer
     */
    @Getter
    private NetworkStats networkStats;

    @Getter
    private PeerType peerType;

    /**
    @Builder(builderMethodName = "peerInfoBuilder")
    PeerInfo(String nodeId, UInt16 port, InetAddress ipAddress, PeerCatchupStatus catchupStatus, NetworkStats networkStats, PeerType peerType) {
        super(nodeId, port, ipAddress, catchupStatus);
    }
    */

    /**
     * Gets catchup status of the peer
     * Only populated if {@link PeerType} of node is {@link PeerType#NODE}
     * @return {@link PeerCatchupStatus} of the peer
     */
    @Override
    public PeerCatchupStatus getCatchupStatus() {
        if (!this.peerType.equals(PeerType.NODE)) {
            throw new IllegalStateException("Catchup Status only avaliable for regular nodes");
        }
        return super.getCatchupStatus();
    }

    /**
     * Parses {@link PeersInfo} to an {@link ImmutableList} of {@link PeerInfo}
     * @param peersInfo {@link PeersInfo} returned by the Grpc API
     * @return an {@link ImmutableList} of {@link PeerInfo}
     */
    public static ImmutableList<PeerInfo> parseToList(PeersInfo peersInfo) throws UnknownHostException{
        val list = new ImmutableList.Builder<PeerInfo>();

        for (PeersInfo.Peer peer : peersInfo.getPeersList()) {
            list.add(parse(peer));
        }
        return list.build();
    }

    /**
     * Parses {@link com.concordium.grpc.v2.PeersInfo.Peer} to {@link PeerInfo}
     * @param peer {@link com.concordium.grpc.v2.PeersInfo.Peer} from Grpc API
     * @return parsed {@link PeerInfo}
     */
    private static PeerInfo parse(PeersInfo.Peer peer) throws UnknownHostException {
        val catchupStatus = (peer.hasNodeCatchupStatus() ? PeerCatchupStatus.parse(peer.getNodeCatchupStatus()) : null);
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
