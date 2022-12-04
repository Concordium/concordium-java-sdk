package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;

public final class ConfigureBakerKeys {
    static {
        loadNatives();
    }

    static void loadNatives() {
        NativeResolver.loadLib();
    }


    public static ConfigureBakerKeysJniOutput generateConfigureBakerKeysPayload(ConfigureBakerKeysJniInput jniInput) {

        ConfigureBakerKeysResult result = null;
        try {
            val inputJsonString = JsonMapper.INSTANCE.writeValueAsString(jniInput);
            val jsonStr = generateConfigureBakerKeysPayload(inputJsonString);
            result = JsonMapper.INSTANCE.readValue(jsonStr, ConfigureBakerKeysResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isok()) {
            throw CryptoJniException.from(
                    result.getErr().orElse(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));
        }

        return result.getOk().orElseThrow(
                () -> CryptoJniException.from(CryptoJniResultCode.ERROR_UNKNOWN_RESULT_CODE));

    }

    private static native String generateConfigureBakerKeysPayload(String input);

}


