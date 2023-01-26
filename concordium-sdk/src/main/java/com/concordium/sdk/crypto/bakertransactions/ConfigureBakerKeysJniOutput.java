package com.concordium.sdk.crypto.bakertransactions;

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
public class ConfigureBakerKeysJniOutput {
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
    private final String proofSig;
    /**
     * Proof of knowledge of the election secret key.
     */
    private final String proofElection;
    /**
     * Proof of knowledge of the aggregation.
     */
    private final String proofAggregation;

    @SneakyThrows
    public byte[] getBytes() {
        val electionVerifyKeyBytes = Hex.decodeHex(electionVerifyKey);
        val signatureVerifyKeyBytes = Hex.decodeHex(signatureVerifyKey);
        val aggregationVerifyKeyBytes = Hex.decodeHex(aggregationVerifyKey);
        val proofSigBytes = Hex.decodeHex(proofSig);
        val proofElectionBytes = Hex.decodeHex(proofElection);
        val proofAggregationBytes = Hex.decodeHex(proofAggregation);

        val buffer = ByteBuffer.allocate(electionVerifyKeyBytes.length
                + signatureVerifyKeyBytes.length
                + aggregationVerifyKeyBytes.length
                + proofSigBytes.length
                + proofElectionBytes.length
                + proofAggregationBytes.length);

        buffer.put(electionVerifyKeyBytes);
        buffer.put(proofElectionBytes);

        buffer.put(signatureVerifyKeyBytes);
        buffer.put(proofSigBytes);

        buffer.put(aggregationVerifyKeyBytes);
        buffer.put(proofAggregationBytes);

        return buffer.array();
    }

}
