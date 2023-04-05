package com.concordium.sdk.responses.nodeinfo;

import com.concordium.sdk.responses.AccountIndex;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents Baking Committee details for a Node.
 */
@Builder
@Getter
@EqualsAndHashCode
public class BakingCommitteeDetails {

    /**
     * Baker ID of the Node.
     */
    private final AccountIndex bakerId;

    /**
     * Is part of the Finalizing Committee.
     */
    private final boolean isFinalizer;

    /**
     * Parses {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} to {@link BakingCommitteeDetails}.
     * @param value {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} returned from the Grpc API.
     * @return Parsed {@link BakingCommitteeDetails}.
     */
    public static BakingCommitteeDetails parse(ConcordiumP2PRpc.NodeInfoResponse value) {
        return BakingCommitteeDetails.builder()
                .bakerId(AccountIndex.from(value.getConsensusBakerId().getValue()))
                .isFinalizer(value.getConsensusFinalizerCommittee())
                .build();
    }
}
