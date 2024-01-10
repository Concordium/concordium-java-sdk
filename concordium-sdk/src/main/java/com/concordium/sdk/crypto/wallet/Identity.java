package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Identity {

    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    public static String createIdentityRequest(IdentityRequestInput input) {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.createIdentityRequestV1(JsonMapper.INSTANCE.writeValueAsString(input));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) { 
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return result.getOk();
    }

    public static String createIdentityRecoveryRequest(IdentityRecoveryRequestInput input) {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.createIdentityRecoveryRequest(JsonMapper.INSTANCE.writeValueAsString(input));
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
