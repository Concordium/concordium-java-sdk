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
