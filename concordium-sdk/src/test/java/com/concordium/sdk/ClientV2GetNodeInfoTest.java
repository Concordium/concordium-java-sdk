package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.nodeinfo.BakingCommitteeDetails;
import com.concordium.sdk.responses.nodeinfo.BakingStatus;
import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import com.concordium.sdk.responses.nodeinfo.PeerType;

import java.time.Instant;

import static com.concordium.sdk.Constants.UTC_ZONE;


public class ClientV2GetNodeInfoTest {

    //TODO Give meaningful values (if exist)
    private static final String PEER_VERSION = "";
    private static final long LOCAL_TIME = 0;
    private static final long PEER_UPTIME = 0;
    private static final String NODE_ID = "";
    private static final long PEER_TOTAL_SENT = 0;
    private static final long PEER_TOTAL_RECIEVED = 0;
    private static final long AVG_BPS_IN = 0;
    private static final long AVG_BPS_OUT = 0;
    private static final long BAKER_ID = 0;

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
            .setNetworkInfo(
                    NodeInfo.NetworkInfo.newBuilder()
                            .setNodeId(
                                    PeerId.newBuilder()
                                            .setValue(NODE_ID)
                            )
                            .setPeerTotalSent(PEER_TOTAL_SENT)
                            .setPeerTotalReceived(PEER_TOTAL_RECIEVED)
                            .setAvgBpsIn(AVG_BPS_IN)
                            .setAvgBpsOut(AVG_BPS_OUT)
                            .build()
            )
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
}
