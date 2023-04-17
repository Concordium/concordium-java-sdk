package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import com.concordium.sdk.responses.peerlist.NetworkStats;
import com.concordium.sdk.responses.peerlist.PeerInfo;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import org.junit.Before;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientV2GetPeersInfo {

    private static final PeersInfo.Peer.NetworkStats GRPC_NETWORK_STATS_ONE = PeersInfo.Peer.NetworkStats.newBuilder()
            .setPacketsSent(1000)
            .setPacketsReceived(500)
            .setLatency(221)
            .build();
    private static final IpSocketAddress GRPC_SOCKET_ADDRESS_ONE = IpSocketAddress.newBuilder()
            .setIp(IpAddress.newBuilder().setValue("127.0.1.0").build())
            .setPort(Port.newBuilder().setValue(5000).build())
            .build();

    private static final PeersInfo.Peer GRPC_PEER_ONE = PeersInfo.Peer.newBuilder()
            .setPeerId(PeerId.newBuilder().setValue("TestOne").build())
            .setSocketAddress(GRPC_SOCKET_ADDRESS_ONE)
            .setNetworkStats(GRPC_NETWORK_STATS_ONE)
            .setBootstrapper(Empty.newBuilder().build())
            .build();

    private static final IpSocketAddress GRPC_SOCKET_ADDRESS_TWO = IpSocketAddress.newBuilder()
            .setIp(IpAddress.newBuilder().setValue("128.0.1.0").build())
            .setPort(Port.newBuilder().setValue(2000).build())
            .build();;
    private static final PeersInfo.Peer.NetworkStats GRPC_NETWORK_STATS_TWO = PeersInfo.Peer.NetworkStats.newBuilder()
            .setPacketsSent(500)
            .setPacketsReceived(1100)
            .setLatency(50)
            .build();;
    private static final PeersInfo.Peer.CatchupStatus GRPC_NODE_CATCHUP_TWO = PeersInfo.Peer.CatchupStatus.UPTODATE;
    private static final PeersInfo.Peer GRPC_PEER_TWO = PeersInfo.Peer.newBuilder()
            .setPeerId(PeerId.newBuilder().setValue("TestTwo").build())
            .setSocketAddress(GRPC_SOCKET_ADDRESS_TWO)
            .setNetworkStats(GRPC_NETWORK_STATS_TWO)
            .setNodeCatchupStatus(GRPC_NODE_CATCHUP_TWO)
            .build(); ;
    private static final IpSocketAddress GRPC_SOCKET_ADDRESS_THREE = IpSocketAddress.newBuilder()
            .setIp(IpAddress.newBuilder().setValue("localhost").build())
            .setPort(Port.newBuilder().setValue(2001).build())
            .build();;
    private static final PeersInfo.Peer.NetworkStats GRPC_NETWORK_STATS_THREE = PeersInfo.Peer.NetworkStats.newBuilder()
            .setPacketsSent(5000)
            .setPacketsReceived(4000)
            .setLatency(20)
            .build();;
    private static final PeersInfo.Peer.CatchupStatus GRPC_NODE_CATCHUP_THREE = PeersInfo.Peer.CatchupStatus.CATCHINGUP;
    private static final PeersInfo.Peer GRPC_PEER_THREE = PeersInfo.Peer.newBuilder()
            .setPeerId(PeerId.newBuilder().setValue("TestThree").build())
            .setSocketAddress(GRPC_SOCKET_ADDRESS_THREE)
            .setNetworkStats(GRPC_NETWORK_STATS_THREE)
            .setNodeCatchupStatus(GRPC_NODE_CATCHUP_THREE)
            .build();;
    private static final PeersInfo GRPC_PEERS_INFO = PeersInfo.newBuilder()
            .addPeers(GRPC_PEER_ONE)
            .addPeers(GRPC_PEER_TWO)
            .addPeers(GRPC_PEER_THREE)
            .build();

    private static ImmutableList<PeerInfo> CLIENT_PEER_INFO;

    /**
     * Creation of clientside PeerInfo objects can throw exceptions so must be done inside method
     */
    @Before
    private void setupClient() throws UnknownHostException {
        PeerInfo CLIENT_PEER_ONE = PeerInfo.builder()
                .ipAddress(InetAddress.getByName("127.0.1.0"))
                .port(UInt16.from(5000))
                .networkStats(
                        NetworkStats.builder()
                                .packetsSent(UInt64.from(1000))
                                .packetsReceived(UInt64.from(500))
                                .latency(UInt64.from(221))
                                .build()
                )
                .peerType(PeerType.BOOTSTRAPPER)
                .build();

        PeerInfo CLIENT_PEER_TWO = null;
        PeerInfo CLIENT_PEER_THREE = null;
        CLIENT_PEER_INFO = ImmutableList.builder()
                .add(CLIENT_PEER_ONE)
                .add(CLIENT_PEER_TWO)
                .add(CLIENT_PEER_THREE)
                .build();
    }
}
