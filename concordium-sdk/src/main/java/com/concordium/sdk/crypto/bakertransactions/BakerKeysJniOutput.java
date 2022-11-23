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
     * A secret key used by a baker to sign blocks.
     */
    private final String signatureSignKey;
    /**
     * A private key for participating in the election lottery.
     */
    private final String electionPrivateKey;
    /**
     * A secret key used by bakers and finalizers to sign finalization records.
     */
    private final String aggregationSignKey;
}
