package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.blockcertificates.SuccessorProof;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RawFinalizationEntry {

    /**
     * The quorum certificate for the finalized block.
     */
    private final RawQuorumCertificate finalizedQc;

    /**
     * The quorum certificate for the block that finalizes
     * the block that 'finalized_qc' points to.
     */
    private final RawQuorumCertificate successorQc;

    /**
     * A proof that the successor block is an immediate
     * successor of the finalized block.
     */
    private final SuccessorProof successorProof;
}
