package com.concordium.sdk.exceptions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
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
    private final CryptoJniResultCode code;

    /**
     * Creates a new `CryptoJniException` object with the given result code.
     *
     * @param code the result code of the failed cryptographic operation.
     */
    private CryptoJniException(CryptoJniResultCode code) {
        super(code.getErrorMessage());
        this.code = code;
    }

    /**
     * Creates a new `CryptoJniException` object from the given result code.
     *
     * @param code the result code of the failed cryptographic operation.
     * @return a new `CryptoJniException` object.
     */
    public static CryptoJniException from(CryptoJniResultCode code) {
        return new CryptoJniException(code);
    }
}
