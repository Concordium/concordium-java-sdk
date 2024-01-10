package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Credential {
    
    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    public static String createUnsignedCredential(UnsignedCredentialInput input) {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.createUnsignedCredentialV1(JsonMapper.INSTANCE.writeValueAsString(input));


            // TODO Then serialize the result as well.


            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) { 
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return result.getOk();
    }

}
