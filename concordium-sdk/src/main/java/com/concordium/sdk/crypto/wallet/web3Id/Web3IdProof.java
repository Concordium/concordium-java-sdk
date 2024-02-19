package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.wallet.StringResult;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Web3IdProof {
    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    /**
     * Creates a Presentation of a web3IdProof.
     * @param input the input required to generate the Presentiation
     * @return a Presentation serialized as JSON
     */
    public static String getWeb3IdProof(Web3IdProofInput input) {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.createWeb3IdProof(JsonMapper.INSTANCE.writeValueAsString(input));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return result.getResult();
    }
}
