package com.concordium.sdk.crypto.bakertransactions;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class BakerKeysJniOutput {
    /**
     * New public key for participating in the election lottery.
     */
    private final String electionVerifyKey;

    /**
     * New public key for verifying this baker's signatures.
     */
    private final String signatureVerifyKey;
    /**
     * New public key for verifying this baker's signature on finalization records.
     */
    private final String aggregationVerifyKey;
    /**
     * Proof of knowledge of the secret key corresponding to the signature
     * verification key.
     */
    private final String proofSignature;
    /**
     * Proof of knowledge of the election secret key.
     */
    private final String proofElection;
    /**
     * Proof of knowledge of the aggregation.
     */
    private final String proofAggregation;
}
