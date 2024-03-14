package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.List;
import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.wallet.ErrorResult;
import com.concordium.sdk.crypto.wallet.StringResult;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AcceptableRequest {
    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    /**
     * Check that a request is acceptable:
     * That range statements are only on the attributes "dob", "idiDocIssuedAt" or
     * "idDocExpiredAt"
     * That membership statement are only on the attributes "Country of residence",
     * "Nationality", "IdDocType" or "IdDocIssuer"
     * That attribute tags are not reused within a credentialStatement
     *
     * @param input the request that should be checked
     * @throws NotAcceptableException if the request is not acceptable
     * @Throws CryptoJniException if there an error occurs duting the check
     * @returns if the request is acceptable, otherwise throws a
     *          NotAcceptableException
     */
    public static void acceptableRequest(QualifiedRequest request) throws NotAcceptableException {
        ErrorResult result = null;
        try {
            String jsonStr = CryptoJniNative.isAcceptableRequest(JsonMapper.INSTANCE.writeValueAsString(request));
            result = JsonMapper.INSTANCE.readValue(jsonStr, ErrorResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
        if (result.getResult() != null) {
            throw new NotAcceptableException(result.getResult());
        }
    }

    /**
     * Check that a statement is acceptable
     *
     * @param statement      the statement that should be checked
     * @param rangeTag       the attribute tags that may be used for range
     *                       statements
     * @param setTags        the attribute tags that may be used for set statements
     * @param attributeCheck custom check on the value of the statement.
     * @throws NotAcceptableException if the request is not acceptable
     * @Throws CryptoJniException if there an error occurs duting the check
     * @returns if the request is acceptable, otherwise throws a
     *          NotAcceptableException
     */
    public static void acceptableAtomicStatement(AtomicStatement statement, List<String> rangeTags,
            List<String> setTags, AttributeCheck attributeCheck) throws NotAcceptableException {
        StringResult result = null;
        try {

            String jsonStr = CryptoJniNative.isAcceptableAtomicStatement(
                    JsonMapper.INSTANCE.writeValueAsString(statement),
                    JsonMapper.INSTANCE.writeValueAsString(rangeTags),
                    JsonMapper.INSTANCE.writeValueAsString(setTags),
                    new RawAttributeCheck(attributeCheck));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }
        if (result.getResult() != null) {
            throw new NotAcceptableException(result.getResult());
        }
    }

    public static class RawAttributeCheck {
        AttributeCheck checker;

        public RawAttributeCheck(AttributeCheck checker) {
            this.checker = checker;
        }

        public void check_attribute(String tag, String value) throws Exception {
            checker.checkAttribute(tag, JsonMapper.INSTANCE.readValue(value, CredentialAttribute.class));
        }
    }

    public static class NotAcceptableException extends Exception {
        public NotAcceptableException(String errorMessage) {
            super(errorMessage);
        }
    }
}
