package com.concordium.sdk.crypto.wallet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.SHA256;
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

        return JsonMapper.INSTANCE.readValue(result.getOk(), UnsignedCredentialDeploymentInfoWithRandomness.class);
    }

    public static byte[] serializeCredentialDeployment(CredentialDeploymentDetails credentialDeploymentDetails) throws JsonMappingException, JsonProcessingException, DecoderException {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.serializeCredentialDeployment(JsonMapper.INSTANCE.writeValueAsString(credentialDeploymentDetails));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return Hex.decodeHex(result.getOk());
    }

    public static byte[] getCredentialDeploymentSignDigest(CredentialDeploymentDetails credentialDeploymentDetails) throws JsonMappingException, JsonProcessingException, DecoderException {
        byte[] serializedCredentialDeploy = serializeCredentialDeployment(credentialDeploymentDetails);
        return SHA256.hash(serializedCredentialDeploy);
    }

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

        return Hex.decodeHex(result.getOk());
    }

}
