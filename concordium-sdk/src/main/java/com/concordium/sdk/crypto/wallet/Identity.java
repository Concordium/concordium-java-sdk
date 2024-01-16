package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Identity {

    /**
     * Creates an identity request that is to be sent to an identity provider when
     * creating a new identity.
     * @param input the input required to generate an identity request
     * @return an identity request serialized as JSON
     */
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

        return result.getResult();
    }
}
