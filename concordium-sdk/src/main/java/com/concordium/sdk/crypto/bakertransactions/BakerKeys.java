package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.vrf.VRFPublicKey;
import com.concordium.sdk.crypto.vrf.VRFSecretKey;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.accountinfo.Baker;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.TransactionFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.io.IOException;
import java.io.Writer;

@Jacksonized
@Builder
@Data
public final class BakerKeys {
    //static block to load native library
    static {
        NativeResolver.loadLib();
    }

    /**
     * New public key for participating in the election lottery.
     */
    private final VRFPublicKey electionVerifyKey;
    /**
     * A private key for participating in the election lottery.
     */
    private final VRFSecretKey electionPrivateKey;
    /**
     * New public key for verifying this baker's signature on finalization records.
     */
    private final BLSPublicKey aggregationVerifyKey;
    /**
     * A secret key used by bakers and finalizers to sign finalization records.
     */
    private final BLSSecretKey aggregationSignKey;
    /**
     * New public key for verifying this baker's signatures.
     */
    private final ED25519PublicKey signatureVerifyKey;
    /**
     * A secret key used by a baker to sign blocks.
     */
    private final ED25519SecretKey signatureSignKey;

    /**
     * Create a fresh set of baker keys.
     * @return the generated baker keys.
     */
    public static BakerKeys createBakerKeys() {

        BakerKeysResult result = null;
        try {
            //Invoking native method to generate baker keys
            val jsonStr = CryptoJniNative.generateBakerKeys();
            result = JsonMapper.INSTANCE.readValue(jsonStr, BakerKeysResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!result.isSuccess()) {
            throw CryptoJniException.from(
                    result.getErr());
        }

        return result.getOk();

    }


    /**
     * Write the {@link BakerCredentials} to the provided Writer with the supplied baker id.
     * Note that the supplied {@link BakerId} must be the one for the account that sent the
     * {@link com.concordium.sdk.transactions.ConfigureBaker} transaction.
     * <br>
     * The baker id can be looked up via {@link com.concordium.sdk.ClientV2#getAccountInfo(BlockQuery, AccountQuery)} if
     * the account is registered as a baker this will yield a {@link Baker#getBakerId()}
     * @param writer where to output the baker credentials.
     * @param bakerId the baker id
     */
    @SneakyThrows
    public void createBakerCredentials(Writer writer, BakerId bakerId) {
        JsonMapper.INSTANCE.writeValue(writer, new BakerCredentials(bakerId, this));
    }

    @AllArgsConstructor
    private static class BakerCredentials {
        @JsonProperty("bakerId")
        private final BakerId bakerId;
        @JsonUnwrapped
        private final BakerKeys bakerKeys;

    }

}


