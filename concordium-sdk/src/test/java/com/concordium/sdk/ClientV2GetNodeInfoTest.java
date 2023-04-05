package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.nodeinfo.BakingCommitteeDetails;
import com.concordium.sdk.responses.nodeinfo.BakingStatus;
import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import com.concordium.sdk.responses.nodeinfov2.NetworkInfo;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Instant;

import static com.concordium.sdk.Constants.UTC_ZONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ClientV2GetNodeInfoTest {

    //TODO Give meaningful values (if exist)
    private static final String PEER_VERSION = "test";
    private static final long LOCAL_TIME = 1;
    private static final long PEER_UPTIME = 1;
    private static final String NODE_ID = "test";
    private static final long PEER_TOTAL_SENT = 1;
    private static final long PEER_TOTAL_RECIEVED = 1;
    private static final long AVG_BPS_IN = 1;
    private static final long AVG_BPS_OUT = 1;
    private static final long BAKER_ID = 1;
    private static final NodeInfo.NetworkInfo GRPC_NETWORK_INFO = NodeInfo.NetworkInfo.newBuilder()
            .setNodeId(
                    PeerId.newBuilder()
                            .setValue(NODE_ID)
            )
            .setPeerTotalSent(PEER_TOTAL_SENT)
            .setPeerTotalReceived(PEER_TOTAL_RECIEVED)
            .setAvgBpsIn(AVG_BPS_IN)
            .setAvgBpsOut(AVG_BPS_OUT)
            .build();

    //TODO GRPC_NODE_INFO for BOOTSTRAPPER, BAKER, NOT_BAKER etc? - Create Node objects and other large objects seperately?
    private static final NodeInfo GRPC_NODE_INFO = NodeInfo.newBuilder()
            .setPeerVersion(PEER_VERSION)
            .setLocalTime(
                    Timestamp.newBuilder()
                            .setValue(LOCAL_TIME)
            )
            .setPeerUptime(
                    Duration.newBuilder()
                            .setValue(PEER_UPTIME)
            )
            .setNetworkInfo(GRPC_NETWORK_INFO)
            .setNode(
                    NodeInfo.Node.newBuilder()
                            .setActive(
                                    NodeInfo.BakerConsensusInfo.newBuilder()
                                            .setActiveBakerCommitteeInfo(NodeInfo.BakerConsensusInfo.ActiveBakerCommitteeInfo.newBuilder().build())
                                            .setActiveFinalizerCommitteeInfo(NodeInfo.BakerConsensusInfo.ActiveFinalizerCommitteeInfo.newBuilder().build())
                                            .setBakerId(BakerId.newBuilder().setValue(BAKER_ID))
                                            .build()
                            )
                            .build()
            )
            .build();

    // "Standard" node info for a real node that is active in committee and is a finalizer.
    private static final com.concordium.sdk.responses.nodeinfo.NodeInfo EXPECTED_NODE_INFO = com.concordium.sdk.responses.nodeinfo.NodeInfo.builder()
            .nodeId(NODE_ID)
            .localTime(Instant.EPOCH.plusSeconds(LOCAL_TIME).atZone(UTC_ZONE))
            .peerType(PeerType.NODE) //GRPC_NODE_INFO is not a bootstrapper
            .consensusState(ConsensusState.ACTIVE)
            .bakingStatus(BakingStatus.ACTIVE_IN_COMMITTEE)
            .committeeDetails(
                    BakingCommitteeDetails.builder()
                            .bakerId(com.concordium.sdk.responses.AccountIndex.from(BAKER_ID))
                            .isFinalizer(true)
                            .build()
            )
            .build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getNodeInfo (Empty request, StreamObserver<NodeInfo> responseObserver) {
                    responseObserver.onNext(GRPC_NODE_INFO);
                    responseObserver.onCompleted();
                }
            }
    ));

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private ClientV2 client;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Credentials.builder().build());
    }

    @Test
    public void getFinalizerNode() {
        var res = client.getNodeInfo();

        verify(serviceImpl).getNodeInfo(any(Empty.class),any(StreamObserver.class));

        // Hvorfor er det toString i consensus test
        assertEquals(EXPECTED_NODE_INFO, res);
    }

    @Test
    public void foo() {
        val nodeInfo = com.concordium.sdk.responses.nodeinfov2.NodeInfo.builder()
                .peerVersion(PEER_VERSION)
                .localTime(Instant.EPOCH.plusSeconds(LOCAL_TIME).atZone(UTC_ZONE))
                .peerUptime(java.time.Duration.ofMillis(PEER_UPTIME))
                .networkInfo(NetworkInfo.parse(GRPC_NETWORK_INFO))
                .node(null)
                .peerType(PeerType.NODE).build();

    }
}
