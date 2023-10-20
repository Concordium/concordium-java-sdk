package com.concordium.sdk.responses.blockcertificates;

import com.concordium.sdk.responses.Epoch;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The epoch finalization entry is the proof that makes the protocol able to advance to a new epoch.
 * I.e. the {@link EpochFinalizationEntry} is present if and only if the block is the first block of a new {@link Epoch}.
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode

public class EpochFinalizationEntry {
    /**
     * The quorum certificate for the finalized block.
     */
    private final QuorumCertificate finalizedQc;
    /**
     * The quorum certificate for the block that finalizes the block that {@link EpochFinalizationEntry#finalizedQc} points to.
     */
    private final QuorumCertificate successorQc;
    /**
     * A proof that the successor block is an immediate successor of the finalized block.
     */
    private final SuccessorProof successorProof;

    /**
     * Parses {@link com.concordium.grpc.v2.EpochFinalizationEntry} to {@link EpochFinalizationEntry}.
      * @param entry {@link com.concordium.grpc.v2.EpochFinalizationEntry} returned by the GRPC v2 API.
     * @return parsed {@link EpochFinalizationEntry}.
     */
    public static EpochFinalizationEntry from(com.concordium.grpc.v2.EpochFinalizationEntry entry) {
        return EpochFinalizationEntry.builder()
                .finalizedQc(QuorumCertificate.from(entry.getFinalizedQc()))
                .successorQc(QuorumCertificate.from(entry.getSuccessorQc()))
                .successorProof(SuccessorProof.from(entry.getSuccessorProof()))
                .build();
    }
}
