package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.grpc.v2.NodeInfo;
import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Node {

    private ConsensusState consensusState;

    /**
     * Null if consensusState isn't ACTIVE
     */
    private BakerConsensusInfo bakerInfo;

    public static Node parse(NodeInfo.NetworkInfo) {
        ConsensusState state;

    }
}
