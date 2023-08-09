package com.concordium.sdk.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the different types of possible errors when performing operations using the JNI.
 */
public enum JNIErrorType {
    @JsonProperty("ParameterSerializationError")
    PARAMETER_SERIALIZATION_ERROR,
    @JsonProperty("Utf8DecodeError")
    UTF8_DECODE_ERROR,
    @JsonProperty("JsonDeserializationError")
    JSON_DESERIALIZATION_ERROR ,
    @JsonProperty("NativeConversionError")
    NATIVE_CONVERSION_ERROR,
    @JsonProperty("PayloadCreationError")
    PAYLOAD_CREATION_ERROR
}
