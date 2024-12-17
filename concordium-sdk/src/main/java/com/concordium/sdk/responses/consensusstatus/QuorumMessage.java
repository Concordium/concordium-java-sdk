package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.FinalizerIndex;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.responses.blockcertificates.QuorumSignature;
import com.concordium.sdk.transactions.Hash;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The message that is multicast by a finalizer when validating and signing a block.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public class QuorumMessage {

    /**
     * Signature on the relevant quorum signature message.
     */
    private final QuorumSignature signature;

    /**
     * Hash of the block that is signed.
     */
    private final Hash block;

    /**
     * Index of the finalizer signing the message.
     */
    private final FinalizerIndex finalizer;

    /**
     * Round of the block.
     */
    private final Round round;

    /**
     * Epoch of the block.
     */
    private final Epoch epoch;
}
