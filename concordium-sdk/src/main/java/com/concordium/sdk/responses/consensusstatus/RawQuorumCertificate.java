package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.FinalizerIndex;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.responses.blockcertificates.QuorumSignature;
import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableList;
import lombok.*;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RawQuorumCertificate {

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
    private final QuorumSignature aggregatedSignature;

    /**
     * A list of the finalizers that formed the quorum certificate
     * i.e., the ones who have contributed to the 'aggregate_signature'.
     * The finalizers are identified by their finalizer index, which refers to the
     * finalization committee for the epoch.
     */
    @Singular
    private final ImmutableList<FinalizerIndex> signatories;
}
