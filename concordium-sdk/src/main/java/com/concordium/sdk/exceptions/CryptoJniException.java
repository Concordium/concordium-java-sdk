package com.concordium.sdk.exceptions;

import lombok.Getter;

/**
 * Represents an exception that is thrown when a cryptographic operation performed using the JNI (Java Native Interface)
 * fails. It has a field, `code`, that contains the result code of the failed operation.
 */
public final class CryptoJniException extends RuntimeException {
    /**
     * The result code of the failed cryptographic operation.
     */
    @Getter
    private final JNIErrorType errorType;
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
