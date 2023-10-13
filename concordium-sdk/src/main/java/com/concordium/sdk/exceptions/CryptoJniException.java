package com.concordium.sdk.exceptions;

import lombok.Getter;

/**
 * Represents an exception that is thrown when an operation performed using the JNI (Java Native Interface)
 * fails.
 */
public final class CryptoJniException extends RuntimeException {
    /**
     * The type of the error.
     */
    @Getter
    private final JNIErrorType errorType;
    /**
     * The error message returned from the JNI.
     */
    @Getter
    private final String errorMessage;


    CryptoJniException(JNIError error) {
        super(error.getErrorType() + ": " + error.getErrorMessage());
        this.errorType = error.getErrorType();
        this.errorMessage = error.getErrorMessage();
    }

    /**
     * Create a new CryptoJniException from the {@link JNIError}.
     * @param error {@link JNIError} returned from the rust layer.
     * @return {@link CryptoJniException} matching the {@link JNIError}.
     */
    public static CryptoJniException from(JNIError error) {
        return new CryptoJniException(error);
    }
}
