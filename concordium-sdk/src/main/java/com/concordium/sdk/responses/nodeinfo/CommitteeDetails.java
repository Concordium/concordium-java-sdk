package com.concordium.sdk.responses.nodeinfo;

import com.concordium.sdk.types.UInt64;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents Baking Committee details for a Node.
 */
@Builder
@Getter
public class CommitteeDetails {

    /**
     * Baker ID of the Node.
     */
    private final UInt64 bakerId;

    /**
     * Is part of the Finalizing Committee.
     */
    private final boolean isFinalizer;

    /**
     * Parses {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} to {@link CommitteeDetails}.
     * @param value {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} returned from the Grpc API.
     * @return Parsed {@link CommitteeDetails}.
     */
    public static CommitteeDetails parse(ConcordiumP2PRpc.NodeInfoResponse value) {
        return CommitteeDetails.builder()
                .bakerId(UInt64.from(value.getConsensusBakerId().getValue()))
                .isFinalizer(value.getConsensusFinalizerCommittee())
                .build();
    }
}
