package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.FinalizerIndex;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.responses.blockcertificates.TimeoutSignature;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A timeout message including the sender's signature.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public class TimeoutMessage {

    /**
     * Index of the finalizer signing the message.
     */
    private final FinalizerIndex finalizer;

    /**
     * Round which timed out.
     */
    private final Round round;

    /**
     * Current epoch number of the finalizer sending the timeout message.
     * This can be different from the epoch of the quorum certificate.
     */
    private final Epoch epoch;

    /**
     * Highest quorum certificate known to the finalizer at the time of timeout.
     */
    private final RawQuorumCertificate quorumCertificate;

    /**
     * Signature on the appropriate timeout signature message.
     */
    private final TimeoutSignature signature;

    /**
     * Signature of the finalizer on the timeout message as a whole.
     */
    private final BlockSignature messageSignature;
}
