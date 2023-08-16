package com.concordium.sdk.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;


/**
 * Represents error values returned from the JNI. Used to instantiate {@link CryptoJniException}.
 */
@Getter
@ToString
public class JNIError {

    private final String errorMessage;
    private final JNIErrorType errorType;

    @JsonCreator
    public JNIError(
            @JsonProperty("errorType") JNIErrorType type,
            @JsonProperty("errorMessage") String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorType = type;
    }
}
