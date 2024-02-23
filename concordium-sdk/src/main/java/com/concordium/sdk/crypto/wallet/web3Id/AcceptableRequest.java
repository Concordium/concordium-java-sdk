package com.concordium.sdk.crypto.wallet.web3Id;

import java.text.AttributedCharacterIterator.Attribute;
import java.util.List;
import java.util.concurrent.atomic.AtomicStampedReference;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.wallet.StringResult;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AcceptableRequest {
    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    /**
     * Check that a request is acceptable
     * @param input the request that should be checked
     * @return a Presentation serialized as JSON
     */
    public static void acceptableRequest(QualifiedRequest request) {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.isAcceptableRequest(JsonMapper.INSTANCE.writeValueAsString(request));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
    }

    /**
     * Check that a request is acceptable
     * @param input the request that should be checked
     * @return a Presentation serialized as JSON
     */
    public static void acceptableAtomicStatement(AtomicStatement statement, List<String> rangeTags, List<String> setTags, AttributeCheck attributeCheck) {
        StringResult result = null;
        try {
            String jsonStr = CryptoJniNative.isAcceptableAtomicStatement(JsonMapper.INSTANCE.writeValueAsString(statement), rangeTags.toString(), setTags.toString(), new RawAttributeCheck(attributeCheck));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
    }

    public static class RawAttributeCheck {
        AttributeCheck checker;

        public RawAttributeCheck(AttributeCheck checker) {
            this.checker = checker;
        }
       
        public String check_attribute(int tag, String value) throws JsonMappingException, JsonProcessingException {
            // TODO fix tag
            return checker.checkAttribute(AttributeType.values()[tag].name(), JsonMapper.INSTANCE.readValue(value, CredentialAttribute.class));
        }
    }
}
