package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import com.concordium.sdk.responses.peerlist.NetworkStats;
import com.concordium.sdk.responses.peerlist.PeerCatchupStatus;
import com.concordium.sdk.responses.peerlist.PeerInfo;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import com.google.common.collect.ImmutableList;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * The test asserts that {@link ClientV2#getPeersInfo()} correctly converts {@link PeersInfo} to {@link ImmutableList} of {@link PeerInfo}.
 */
public class ClientV2GetPeersInfoTest {

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
            .build();

    private static final PeersInfo.Peer.NetworkStats GRPC_NETWORK_STATS_TWO = PeersInfo.Peer.NetworkStats.newBuilder()
            .setPacketsSent(500)
            .setPacketsReceived(1100)
            .setLatency(50)
            .build();

    private static final PeersInfo.Peer.CatchupStatus GRPC_NODE_CATCHUP_TWO = PeersInfo.Peer.CatchupStatus.UPTODATE;
    private static final PeersInfo.Peer GRPC_PEER_TWO = PeersInfo.Peer.newBuilder()
            .setPeerId(PeerId.newBuilder().setValue("TestTwo").build())
            .setSocketAddress(GRPC_SOCKET_ADDRESS_TWO)
            .setNetworkStats(GRPC_NETWORK_STATS_TWO)
            .setNodeCatchupStatus(GRPC_NODE_CATCHUP_TWO)
            .build();

    private static final IpSocketAddress GRPC_SOCKET_ADDRESS_THREE = IpSocketAddress.newBuilder()
            .setIp(IpAddress.newBuilder().setValue("localhost").build())
            .setPort(Port.newBuilder().setValue(2001).build())
            .build();

    private static final PeersInfo.Peer.NetworkStats GRPC_NETWORK_STATS_THREE = PeersInfo.Peer.NetworkStats.newBuilder()
            .setPacketsSent(5000)
            .setPacketsReceived(4000)
            .setLatency(20)
            .build();

    private static final PeersInfo.Peer.CatchupStatus GRPC_NODE_CATCHUP_THREE = PeersInfo.Peer.CatchupStatus.CATCHINGUP;
    private static final PeersInfo.Peer GRPC_PEER_THREE = PeersInfo.Peer.newBuilder()
            .setPeerId(PeerId.newBuilder().setValue("TestThree").build())
            .setSocketAddress(GRPC_SOCKET_ADDRESS_THREE)
            .setNetworkStats(GRPC_NETWORK_STATS_THREE)
            .setNodeCatchupStatus(GRPC_NODE_CATCHUP_THREE)
            .build();

    private static final PeersInfo GRPC_PEERS_INFO = PeersInfo.newBuilder()
            .addPeers(GRPC_PEER_ONE)
            .addPeers(GRPC_PEER_TWO)
            .addPeers(GRPC_PEER_THREE)
            .build();

    private static ImmutableList<PeerInfo> CLIENT_PEER_INFO;

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getPeersInfo(
                        Empty request,
                        StreamObserver<PeersInfo> responseObserver) {
                    responseObserver.onNext(GRPC_PEERS_INFO);
                    responseObserver.onCompleted();
                }
            }
    ));

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private ClientV2 client;


    /**
     * Creation of clientside PeerInfo objects can throw exceptions so must be done inside method
     */
    @Before
    public void setupClient() throws IOException {
        PeerInfo CLIENT_PEER_ONE = PeerInfo.builder()
                .nodeId("TestOne")
                .ipAddress(InetAddress.getByName("127.0.1.0"))
                .port(UInt16.from(5000))
                .networkStats(
                        NetworkStats.builder()
                                .packetsSent(UInt64.from(1000))
                                .packetsReceived(UInt64.from(500))
                                .latency(UInt64.from(221))
                                .build()
                )
                .catchupStatus(PeerCatchupStatus.NOT_AVAILABLE)
                .peerType(PeerType.BOOTSTRAPPER)
                .build();

        PeerInfo CLIENT_PEER_TWO = PeerInfo.builder()
                .nodeId("TestTwo")
                .ipAddress(InetAddress.getByName("128.0.1.0"))
                .port(UInt16.from(2000))
                .networkStats(
                        NetworkStats.builder()
                                .packetsSent(UInt64.from(500))
                                .packetsReceived(UInt64.from(1100))
                                .latency(UInt64.from(50))
                                .build()
                )
                .peerType(PeerType.NODE)
                .catchupStatus(PeerCatchupStatus.UP_TO_DATE)
                .build();

        PeerInfo CLIENT_PEER_THREE = PeerInfo.builder()
                .nodeId("TestThree")
                .ipAddress(InetAddress.getByName("localhost"))
                .port(UInt16.from(2001))
                .networkStats(
                        NetworkStats.builder()
                                .packetsSent(UInt64.from(5000))
                                .packetsReceived(UInt64.from(4000))
                                .latency(UInt64.from(20))
                                .build()
                )
                .peerType(PeerType.NODE)
                .catchupStatus(PeerCatchupStatus.CATCHING_UP)
                .build();

        CLIENT_PEER_INFO = new ImmutableList.Builder<PeerInfo>()
                .add(CLIENT_PEER_ONE)
                .add(CLIENT_PEER_TWO)
                .add(CLIENT_PEER_THREE)
                .build();

        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel);
    }

    @Test
    public void getPeersInfo() throws UnknownHostException {
        val res = client.getPeersInfo();

        verify(serviceImpl).getPeersInfo(any(Empty.class), any(StreamObserver.class));
        assertEquals(CLIENT_PEER_INFO, res);
    }
}
