package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.wallet.credential.CredentialDeploymentDetails;
import com.concordium.sdk.crypto.wallet.credential.UnsignedCredentialDeploymentInfo;
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

        System.out.println(result.getOk());

        return JsonMapper.INSTANCE.readValue(result.getOk(), UnsignedCredentialDeploymentInfoWithRandomness.class);
    }

    public static UnsignedCredentialDeploymentInfo getCredentialDeploymentSignDigest(CredentialDeploymentDetails credentialDeploymentDetails) throws JsonMappingException, JsonProcessingException {
        StringResult result = null;
        try {
            System.out.println(JsonMapper.INSTANCE.writeValueAsString(credentialDeploymentDetails));
            String jsonStr = CryptoJniNative.serializeCredentialDeployment(JsonMapper.INSTANCE.writeValueAsString(credentialDeploymentDetails));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) { 
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return JsonMapper.INSTANCE.readValue(result.getOk(), UnsignedCredentialDeploymentInfo.class);
    }

}
