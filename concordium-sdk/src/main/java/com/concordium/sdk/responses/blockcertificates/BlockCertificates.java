package com.concordium.sdk.responses.blockcertificates;

import com.concordium.sdk.responses.Round;
import lombok.*;

import java.util.Optional;

/**
 * Certificates for a block for protocols supporting ConcordiumBFT.
 * ConcordiumBFT progresses to a new {@link Round} either via a {@link QuorumCertificate} or a {@link TimeoutCertificate} for a particular round.
 */
@EqualsAndHashCode
@Builder
@ToString(doNotUseGetters = true)
public class BlockCertificates {

    /**
     * The quorum certificate. Is present if and only if the block is not a genesis block.
     */
    private final QuorumCertificate quorumCertificate;
    /**
     * The timeout certificate. Is present only if the round prior to the round of the block timed out.
     */
    private final TimeoutCertificate timeoutCertificate;
    /**
     * The epoch finalization entry. Is present only if the block initiates a new epoch.
     */
    private final EpochFinalizationEntry epochFinalizationEntry;

    public Optional<QuorumCertificate> getQuorumCertificate() {
        return Optional.ofNullable(quorumCertificate);
    }

    public Optional<TimeoutCertificate> getTimeoutCertificate() {
        return Optional.ofNullable(timeoutCertificate);
    }

    public Optional<EpochFinalizationEntry> getEpochFinalizationEntry() {
        return Optional.ofNullable(epochFinalizationEntry);
    }

    /**
     * Parses {@link com.concordium.grpc.v2.BlockCertificates} to {@link BlockCertificates}.
     *
     * @param certificates {@link com.concordium.grpc.v2.BlockCertificates} returned by the GRPC v2 API.
     * @return parsed {@link BlockCertificates}.
     */
    public static BlockCertificates from(com.concordium.grpc.v2.BlockCertificates certificates) {
        val b = BlockCertificates.builder();
        if (certificates.hasQuorumCertificate()) {
            b.quorumCertificate(QuorumCertificate.from(certificates.getQuorumCertificate()));
        }
        if (certificates.hasTimeoutCertificate()) {
            b.timeoutCertificate(TimeoutCertificate.from(certificates.getTimeoutCertificate()));
        }
        if (certificates.hasEpochFinalizationEntry()) {
            b.epochFinalizationEntry(EpochFinalizationEntry.from(certificates.getEpochFinalizationEntry()));
        }
        return b.build();
    }
}
