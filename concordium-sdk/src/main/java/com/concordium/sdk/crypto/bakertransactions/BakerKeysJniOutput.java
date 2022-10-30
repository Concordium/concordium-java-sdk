package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.transactions.AddBakerKeysPayload;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.jackson.Jacksonized;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

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
    private final String signatureSignKey;
    /**
     * Proof of knowledge of the election secret key.
     */
    private final String electionPrivateKey;
    /**
     * Proof of knowledge of the aggregation.
     */
    private final String aggregationSignKey;
}
