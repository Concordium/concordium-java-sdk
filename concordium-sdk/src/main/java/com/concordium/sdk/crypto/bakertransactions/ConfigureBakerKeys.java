package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;

public final class ConfigureBakerKeys {
    //static block to load native library
    static {
        NativeResolver.loadLib();
    }

    // Method to generate the payload for configuring baker keys
    public static ConfigureBakerKeysJniOutput generateConfigureBakerKeysPayload(ConfigureBakerKeysJniInput jniInput) {

        ConfigureBakerKeysResult result = null;
        try {
            val inputJsonString = JsonMapper.INSTANCE.writeValueAsString(jniInput);
            //Invoking native method to generate configure baker keys payload
            val jsonStr = CryptoJniNative.generateConfigureBakerKeysPayload(inputJsonString);
            result = JsonMapper.INSTANCE.readValue(jsonStr, ConfigureBakerKeysResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isok()) {
            throw CryptoJniException.from(
                    result.getErr().orElse(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));
        }

        // return the ok field of the result object
        return result.getOk().orElseThrow(
                () -> CryptoJniException.from(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));

    }

}


