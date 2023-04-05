package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.grpc.v2.NodeInfo;
import com.concordium.sdk.responses.AccountIndex;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import lombok.var;

@Getter
@Builder
public class BakerConsensusInfo {

    private BakerConsensusStatus status;

    private AccountIndex bakerId;

    //TODO comment (kan kun parses for nodeInfo med ACTIVE node)
    public static BakerConsensusInfo parse(NodeInfo.BakerConsensusInfo bakerConsensusInfo) {

        AccountIndex bakerId = AccountIndex.from(bakerConsensusInfo.getBakerId().getValue());
        var builder = BakerConsensusInfo.builder()
                .bakerId(bakerId);

        val isActiveBaker = bakerConsensusInfo.hasActiveBakerCommitteeInfo();
        val isActiveFinalizer = bakerConsensusInfo.hasActiveFinalizerCommitteeInfo();

        // Node has baker keys but is not active in committee
        if (! (isActiveBaker || isActiveFinalizer)) {
            return buildPassiveBakerConsensusInfo(bakerConsensusInfo, builder);
        }

        //If node is not finalizer it must be baker
        return (isActiveFinalizer ? builder.status(BakerConsensusStatus.ACTIVE_FINALIZER).build() : builder.status(BakerConsensusStatus.ACTIVE_BAKER).build());
    }

    private static BakerConsensusInfo buildPassiveBakerConsensusInfo(NodeInfo.BakerConsensusInfo bakerConsensusInfo, BakerConsensusInfoBuilder builder) {
        val status = bakerConsensusInfo.getPassiveCommitteeInfo();
        switch (status) {
            case NOT_IN_COMMITTEE:
                builder = builder.status(BakerConsensusStatus.NOT_IN_COMMITTEE);
                break;
            case ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE:
                builder = builder.status(BakerConsensusStatus.ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE);
                break;
            case ADDED_BUT_WRONG_KEYS:
                builder = builder.status(BakerConsensusStatus.ADDED_BUT_WRONG_KEYS);
                break;
        }
        return builder.build();
    }
}
