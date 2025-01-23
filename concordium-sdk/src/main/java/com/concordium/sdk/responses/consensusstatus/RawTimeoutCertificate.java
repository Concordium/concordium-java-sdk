package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.responses.blockcertificates.TimeoutSignature;
import com.google.common.collect.ImmutableList;
import lombok.*;

/**
 * A timeout certificate is the certificate that the
 * finalization committee issues when a round times out,
 * thus making it possible for the protocol to proceed to the
 * next round.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public class RawTimeoutCertificate {

    /**
     * The round that timed out.
     */
    private final Round round;

    /**
     * The minimum epoch of which signatures are included
     * in the 'aggregate_signature'.
     */
    private final Epoch minEpoch;

    /**
     * The rounds of which finalizers have their best
     * QCs in the 'min_epoch'.
     */
    @Singular("qcRoundFirstEpoch")
    private final ImmutableList<RawFinalizerRound> qcRoundsFirstEpoch;

    /**
     * The rounds of which finalizers have their best
     * QCs in the epoch 'min_epoch' + 1.
     */
    @Singular("qcRoundSecondEpoch")
    private final ImmutableList<RawFinalizerRound> qcRoundsSecondEpoch;

    /**
     * The aggregated signature by the finalization committee that witnessed
     * the 'round' timed out.
     */
    private final TimeoutSignature aggregateSignature;
}
