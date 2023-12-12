package com.concordium.sdk.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the different types of possible errors when performing operations using the JNI. Should match the errors defined in the rust code in crypto-jni.
 */
public enum JNIErrorType {
    @JsonProperty("ParameterSerialization")
    PARAMETER_SERIALIZATION_ERROR,
    @JsonProperty("Utf8DecodeError")
    UTF8_DECODE_ERROR,
    @JsonProperty("JsonDeserialization")
    JSON_DESERIALIZATION_ERROR ,
    @JsonProperty("NativeConversion")
    NATIVE_CONVERSION_ERROR,
    @JsonProperty("PayloadCreation")
    PAYLOAD_CREATION_ERROR
}
