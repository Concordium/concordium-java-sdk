package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;

public final class BakerKeys {
    static {
        loadNatives();
    }

    static void loadNatives() {
        NativeResolver.loadLib();
    }


    public static BakerKeysJniOutput createBakerKeys() {

        BakerKeysResult result = null;
        try {
            val jsonStr = generateBakerKeys();
            result = JsonMapper.INSTANCE.readValue(jsonStr, BakerKeysResult.class);
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

    private static native String generateBakerKeys();

}


