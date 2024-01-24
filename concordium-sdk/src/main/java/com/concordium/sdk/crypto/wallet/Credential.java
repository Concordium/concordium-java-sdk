package com.concordium.sdk.crypto.wallet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentDetails;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentSerializationContext;
import com.concordium.sdk.crypto.wallet.credential.UnsignedCredentialDeploymentInfoWithRandomness;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Credential {
    
    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    /**
     * Creates an unsigned credential.
     * @param input the required input for creating an unsigned credential
     * @return an unsigned credential and the randomness used to generate the credential
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    public static UnsignedCredentialDeploymentInfoWithRandomness createUnsignedCredential(UnsignedCredentialInput input) throws JsonMappingException, JsonProcessingException {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.createUnsignedCredentialV1(JsonMapper.INSTANCE.writeValueAsString(input));            
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) { 
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return JsonMapper.INSTANCE.readValue(result.getResult(), UnsignedCredentialDeploymentInfoWithRandomness.class);
    }

    /**
     * Computes the sign digest on the serialized credential deployment details, which is the value that must be signed
     * to able to construct the signed credential deployment transaction that is sent to the Concordium node.
     * @param credentialDeploymentDetails the details for the credential deployment
     * @return the credential deployment transaction sign digest
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws DecoderException
     */
    public static byte[] getCredentialDeploymentSignDigest(CredentialDeploymentDetails credentialDeploymentDetails) throws JsonMappingException, JsonProcessingException, DecoderException {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.computeCredentialDeploymentSignDigest(JsonMapper.INSTANCE.writeValueAsString(credentialDeploymentDetails));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return Hex.decodeHex(result.getResult());
    }

    /**
     * Serializes a credential deployment payload which is the payload to be sent to the Concordium node along
     * with the transaction expiry.
     * @param context the credential deployment serialization context, including the signatures on the credential deployment sign digest.
     * @return the serialized credential deployment payload, to be sent as the rawPayload in {@link ClientV2#sendCredentialDeploymentTransaction(com.concordium.sdk.transactions.TransactionExpiry, byte[])}
     * @throws DecoderException
     */
    public static byte[] serializeCredentialDeploymentPayload(CredentialDeploymentSerializationContext context) throws DecoderException {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.serializeCredentialDeploymentForSubmission(JsonMapper.INSTANCE.writeValueAsString(context));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return Hex.decodeHex(result.getResult());
    }
}
