package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import com.concordium.sdk.responses.nodeinfov2.BakerConsensusInfo;
import com.concordium.sdk.responses.nodeinfov2.BakingCommitteeStatus;
import com.concordium.sdk.responses.nodeinfov2.NetworkInfo;
import com.concordium.sdk.responses.nodeinfov2.Node;
import com.concordium.sdk.types.UInt64;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * The test asserts that {@link ClientV2#getNodeInfo()}
 * correctly converts the {@link NodeInfo} returned by the server to {@link com.concordium.sdk.responses.nodeinfov2.NodeInfo}.
 */
public class ClientV2GetNodeInfoTest {

    // ------------- Test Values -------------
    private static final String PEER_VERSION = "test";
    private static final long LOCAL_TIME = 1;
    private static final long PEER_UPTIME = 1;
    private static final String NODE_ID = "test";
    private static final long PEER_TOTAL_SENT = 1;
    private static final long PEER_TOTAL_RECEIVED = 1;
    private static final long AVG_BPS_IN = 1;
    private static final long AVG_BPS_OUT = 1;
    private static final long BAKER_ID = 1;

    // ------------- "Standard" objects the same for all kinds of NodeInfo -------------
    private static final NodeInfo.NetworkInfo GRPC_NETWORK_INFO = NodeInfo.NetworkInfo.newBuilder()
            .setNodeId(
                    PeerId.newBuilder()
                            .setValue(NODE_ID)
            )
            .setPeerTotalSent(PEER_TOTAL_SENT)
            .setPeerTotalReceived(PEER_TOTAL_RECEIVED)
            .setAvgBpsIn(AVG_BPS_IN)
            .setAvgBpsOut(AVG_BPS_OUT)
            .build();

    private static final NetworkInfo CLIENT_NETWORK_INFO = NetworkInfo.builder()
            .nodeID(NODE_ID)
            .peerTotalSent(UInt64.from(PEER_TOTAL_SENT))
            .peerTotalReceived(UInt64.from(PEER_TOTAL_RECEIVED))
            .avgBpsIn(UInt64.from(AVG_BPS_IN))
            .avgBpsOut(UInt64.from(AVG_BPS_OUT))
            .build();
    // Builder for gRPC NodeInfo with "standard" fields set
    private static final NodeInfo.Builder GRPC_BUILDER = NodeInfo.newBuilder()
            .setPeerVersion(PEER_VERSION)
            .setLocalTime(
                    Timestamp.newBuilder()
                            .setValue(LOCAL_TIME)
            )
            .setPeerUptime(
                    Duration.newBuilder()
                            .setValue(PEER_UPTIME)
            )
            .setNetworkInfo(GRPC_NETWORK_INFO);

    // Builder for clientside NodeInfo with "standard" fields set
    private static final com.concordium.sdk.responses.nodeinfov2.NodeInfo.NodeInfoBuilder CLIENT_BUILDER = com.concordium.sdk.responses.nodeinfov2.NodeInfo.builder()
            .peerVersion(PEER_VERSION)
            .localTime(com.concordium.sdk.types.Timestamp.newMillis(LOCAL_TIME))
            .peerUptime(java.time.Duration.ofMillis(PEER_UPTIME))
            .networkInfo(CLIENT_NETWORK_INFO);

    // ------------- Node Objects -------------
    private static final NodeInfo.Node GRPC_FINALIZER_NODE = NodeInfo.Node.newBuilder()
            .setActive(
                    NodeInfo.BakerConsensusInfo.newBuilder()
                            .setActiveBakerCommitteeInfo(NodeInfo.BakerConsensusInfo.ActiveBakerCommitteeInfo.newBuilder().build())
                            .setActiveFinalizerCommitteeInfo(NodeInfo.BakerConsensusInfo.ActiveFinalizerCommitteeInfo.newBuilder().build())
                            .setBakerId(BakerId.newBuilder().setValue(BAKER_ID))
                            .build()
            )
            .build();

    private static final NodeInfo.Node GRPC_BAKER_NODE = NodeInfo.Node.newBuilder()
            .setActive(
                    NodeInfo.BakerConsensusInfo.newBuilder()
                            .setActiveBakerCommitteeInfo(NodeInfo.BakerConsensusInfo.ActiveBakerCommitteeInfo.newBuilder().build())
                            .setBakerId(BakerId.newBuilder().setValue(BAKER_ID))
                            .build()
            )
            .build();

    private static final NodeInfo.Node GRPC_PASSIVE_COMMITTEE_NODE = NodeInfo.Node.newBuilder()
            .setActive(
                    NodeInfo.BakerConsensusInfo.newBuilder()
                            .setPassiveCommitteeInfo(NodeInfo.BakerConsensusInfo.PassiveCommitteeInfo.ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE)
                            .setBakerId(BakerId.newBuilder().setValue(BAKER_ID))
                            .build()
            )
            .build();

    private static final NodeInfo.Node GRPC_PASSIVE_NODE = NodeInfo.Node.newBuilder()
            .setPassive(Empty.newBuilder().build())
            .build();
    private static final Node CLIENT_FINALIZER_NODE = Node.builder()
            .consensusState(ConsensusState.ACTIVE)
            .bakerInfo(BakerConsensusInfo.builder()
                    .bakerId(com.concordium.sdk.responses.AccountIndex.from(BAKER_ID))
                    .status(BakingCommitteeStatus.ACTIVE_FINALIZER)
                    .build()
            )
            .build();

    private static final Node CLIENT_BOOTSTRAPPER_NODE = Node.builder()
            .consensusState(ConsensusState.NOT_RUNNING)
            .bakerInfo(null)
            .build();

    private static final Node CLIENT_BAKER_NODE = Node.builder()
            .consensusState(ConsensusState.ACTIVE)
            .bakerInfo(BakerConsensusInfo.builder()
                    .bakerId(com.concordium.sdk.responses.AccountIndex.from(BAKER_ID))
                    .status(BakingCommitteeStatus.ACTIVE_BAKER)
                    .build()
            )
            .build();

    private static final Node CLIENT_PASSIVE_COMMITTEE_NODE = Node.builder()
            .consensusState(ConsensusState.ACTIVE)
            .bakerInfo(BakerConsensusInfo.builder()
                    .bakerId(com.concordium.sdk.responses.AccountIndex.from(BAKER_ID))
                    .status(BakingCommitteeStatus.ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE)
                    .build()
            )
            .build();

    private static final Node CLIENT_PASSIVE_NODE = Node.builder()
            .consensusState(ConsensusState.PASSIVE)
            .bakerInfo(null)
            .build();
    // ------------- NodeInfo Objects -------------
    private static final NodeInfo GRPC_BOOTSTRAPPER_NODE_INFO = GRPC_BUILDER
            .setBootstrapper(Empty.newBuilder().build())
            .build();

    private static final NodeInfo GRPC_FINALIZER_NODE_INFO = GRPC_BUILDER
            .setNode(GRPC_FINALIZER_NODE)
            .build();

    private static final NodeInfo GRPC_BAKER_NODE_INFO = GRPC_BUILDER
            .setNode(GRPC_BAKER_NODE)
            .build();

    private static final NodeInfo GRPC_PASSIVE_COMMITTEE_NODE_INFO = GRPC_BUILDER
            .setNode(GRPC_PASSIVE_COMMITTEE_NODE)
            .build();

    private static final NodeInfo GRPC_PASSIVE_NODE_INFO = GRPC_BUILDER
            .setNode(GRPC_PASSIVE_NODE)
            .build();

    // "Standard" node info for a real node that is active in committee and is a finalizer.
    private static final com.concordium.sdk.responses.nodeinfov2.NodeInfo CLIENT_FINALIZER_NODE_INFO = CLIENT_BUILDER
            .node(CLIENT_FINALIZER_NODE)
            .peerType(PeerType.NODE)
            .build();

    // Node info for BOOTSTRAPPER node
    private static final com.concordium.sdk.responses.nodeinfov2.NodeInfo CLIENT_BOOTSTRAPPER_NODE_INFO = CLIENT_BUILDER
            .node(CLIENT_BOOTSTRAPPER_NODE)
            .peerType(PeerType.BOOTSTRAPPER)
            .build();

    // Node info for baker node that is active in committee and not finalizer
    private static final com.concordium.sdk.responses.nodeinfov2.NodeInfo CLIENT_BAKER_NODE_INFO = CLIENT_BUILDER
            .node(CLIENT_BAKER_NODE)
            .peerType(PeerType.NODE)
            .build();

    private static final com.concordium.sdk.responses.nodeinfov2.NodeInfo CLIENT_PASSIVE_COMMITTEE_NODE_INFO = CLIENT_BUILDER
            .node(CLIENT_PASSIVE_COMMITTEE_NODE)
            .peerType(PeerType.NODE)
            .build();

    private static final com.concordium.sdk.responses.nodeinfov2.NodeInfo CLIENT_PASSIVE_NODE_INFO = CLIENT_BUILDER
            .node(CLIENT_PASSIVE_NODE)
            .peerType(PeerType.NODE)
            .build();

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private ClientV2 client;

    // Must be first line of every test as it acts as a setup method as well
    private QueriesGrpc.QueriesImplBase configureResponseAndSetup(NodeInfo response) throws Exception {
        QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
                new QueriesGrpc.QueriesImplBase() {
                    @Override
                    public void getNodeInfo(Empty request, StreamObserver<NodeInfo> responseObserver) {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                }));
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
        return serviceImpl;
    }


    @Test
    public void getFinalizerNode() throws Exception {
        val serviceImpl = configureResponseAndSetup(GRPC_FINALIZER_NODE_INFO);
        val res = client.getNodeInfo();

        verify(serviceImpl).getNodeInfo(any(Empty.class), any(StreamObserver.class));

        assertEquals(CLIENT_FINALIZER_NODE_INFO, res);
    }

    @Test
    public void getBootstrapperNode() throws Exception {
        val serviceImpl = configureResponseAndSetup(GRPC_BOOTSTRAPPER_NODE_INFO);
        val res = client.getNodeInfo();

        verify(serviceImpl).getNodeInfo(any(Empty.class), any(StreamObserver.class));

        assertEquals(CLIENT_BOOTSTRAPPER_NODE_INFO, res);
    }

    @Test
    public void getBakerNode() throws Exception {
        val serviceImpl = configureResponseAndSetup(GRPC_BAKER_NODE_INFO);
        val res = client.getNodeInfo();

        verify(serviceImpl).getNodeInfo(any(Empty.class), any(StreamObserver.class));

        assertEquals(CLIENT_BAKER_NODE_INFO, res);
    }

    @Test
    public void getPassiveCommitteeNode() throws Exception {
        val serviceImpl = configureResponseAndSetup(GRPC_PASSIVE_COMMITTEE_NODE_INFO);
        val res = client.getNodeInfo();

        verify(serviceImpl).getNodeInfo(any(Empty.class), any(StreamObserver.class));

        assertEquals(CLIENT_PASSIVE_COMMITTEE_NODE_INFO, res);
    }

    @Test
    public void getPassiveNode() throws Exception {
        val serviceImpl = configureResponseAndSetup(GRPC_PASSIVE_NODE_INFO);
        val res = client.getNodeInfo();

        verify(serviceImpl).getNodeInfo(any(Empty.class), any(StreamObserver.class));

        assertEquals(CLIENT_PASSIVE_NODE_INFO, res);
    }

}
