package com.concordium.sdk.responses.nodeinfov2;

import com.concordium.grpc.v2.NodeInfo;
import com.concordium.sdk.responses.AccountIndex;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@ToString

/*
 * Consensus info for a node configured with baker keys.
 * Only present in {@link Node} configured with baker keys
 */
public class BakerConsensusInfo {

    /**
     * Status of the baker configured node
     */
    private BakingCommitteeStatus status;

    /**
     * Baker ID of the node
     */
    private AccountIndex bakerId;


    /**
     * Parses {@link com.concordium.grpc.v2.NodeInfo.BakerConsensusInfo} to {@link BakerConsensusInfo}
     * Only possible for NodeInfo with ACTIVE node
     * @param bakerConsensusInfo {@link com.concordium.grpc.v2.NodeInfo.BakerConsensusInfo} returned from the Grpc API
     * @return Parsed {@link BakerConsensusInfo}
     */
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
        return (isActiveFinalizer ? builder.status(BakingCommitteeStatus.ACTIVE_FINALIZER).build() : builder.status(BakingCommitteeStatus.ACTIVE_BAKER).build());
    }

    /**
     * Helper methhod for the parse method. Creates {@link BakerConsensusInfo} that is not active in the committee
     * @param bakerConsensusInfo {@link com.concordium.grpc.v2.NodeInfo.BakerConsensusInfo} returned from the Grpc API
     * @param builder {@link BakerConsensusInfoBuilder} with bakerId configured
     * @return {@link BakerConsensusInfo} for baker node not active in the committee
     */
    private static BakerConsensusInfo buildPassiveBakerConsensusInfo(NodeInfo.BakerConsensusInfo bakerConsensusInfo, BakerConsensusInfoBuilder builder) {
        val status = bakerConsensusInfo.getPassiveCommitteeInfo();
        switch (status) {
            case NOT_IN_COMMITTEE:
                builder = builder.status(BakingCommitteeStatus.NOT_IN_COMMITTEE);
                break;
            case ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE:
                builder = builder.status(BakingCommitteeStatus.ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE);
                break;
            case ADDED_BUT_WRONG_KEYS:
                builder = builder.status(BakingCommitteeStatus.ADDED_BUT_WRONG_KEYS);
                break;
        }
        return builder.build();
    }
}
