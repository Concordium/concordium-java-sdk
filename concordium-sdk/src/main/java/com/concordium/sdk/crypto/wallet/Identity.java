package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Identity {

    public static String createIdentityRequest(IdentityRequestInput input) {
        KeyResult result = null;
        try {
            String jsonStr = CryptoJniNative.createIdRequestWithKeysV1(JsonMapper.INSTANCE.writeValueAsString(input));

            // TODO Do not use key result, or re-name it to StringResult or something more generic.
            result = JsonMapper.INSTANCE.readValue(jsonStr, KeyResult.class);
        } catch (JsonProcessingException e) { 
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return result.getOk();
    }
}
