package com.concordium.sdk.responses.blockcertificates;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.transactions.Hash;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A quorum certificate is the certificate that the finalization committee issues in order to certify a block.
 * A block must be certified before it will be part of the authoritative part of the chain.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class QuorumCertificate {

    /**
     * The hash of the block that the quorum certificate refers to.
     */
    private final Hash blockHash;
    /**
     * The round of the block.
     */
    private final Round round;
    /**
     * The epoch of the block.
     */
    private final Epoch epoch;
    /**
     * The aggregated signature by the finalization committee on the block.
     */
    private final QuorumSignature aggregateSignature;
    /**
     * A list of the finalizers that formed the quorum certificate i.e., the ones who have contributed to the {@link QuorumCertificate#aggregateSignature}.
     * The finalizers are identified by their {@link BakerId} as this is stable across protocols and epochs.
     */
    private final List<BakerId> signatories;

    /**
     * Parses {@link com.concordium.grpc.v2.QuorumCertificate} to {@link QuorumCertificate}.
     * @param certificate {@link com.concordium.grpc.v2.QuorumCertificate} returned by the GRPC v2 API.
     * @return parsed {@link QuorumCertificate}.
     */
    public static QuorumCertificate from(com.concordium.grpc.v2.QuorumCertificate certificate) {
        val signatories = new ArrayList<BakerId>();
        certificate.getSignatoriesList().forEach(
                s -> signatories.add(BakerId.from(s))
        );
        return QuorumCertificate.builder()
                .blockHash(Hash.from(certificate.getBlockHash()))
                .round(Round.from(certificate.getRound()))
                .epoch(Epoch.from(certificate.getEpoch()))
                .aggregateSignature(QuorumSignature.from(certificate.getAggregateSignature()))
                .signatories(signatories)
                .build();
    }
}
