package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;

public class BakerKeys {
    static {
        loadNatives();
    }

    public static void main(String[] args) {
        createBakeKeys();
    }

    static void loadNatives() {
        NativeResolver.loadLib();
    }


    public static BakerKeysJniOutput createBakeKeys() {

        BakerKeysJniOutput result = null;
        try {
            val buff = new byte[128];
            val resultCode = generateBakerKeys(buff);
            if (resultCode > 0) {
                val errString = new String(buff);
                throw new Exception("Error: " + errString + ", ErrorCode: " + resultCode);
            }
            val jsonStr = new String(buff);
            result = JsonMapper.INSTANCE.readValue(jsonStr, BakerKeysJniOutput.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static native int generateBakerKeys(byte[] buffer);

}


