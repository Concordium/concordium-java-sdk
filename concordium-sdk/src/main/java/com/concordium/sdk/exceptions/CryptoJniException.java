package com.concordium.sdk.exceptions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import lombok.Getter;

public final class CryptoJniException extends RuntimeException {
    @Getter
    private final CryptoJniResultCode code;

    private CryptoJniException(CryptoJniResultCode code) {
        super(code.getErrorMessage());
        this.code = code;
    }

    public static CryptoJniException from(CryptoJniResultCode code) {
        return new CryptoJniException(code);
    }
}
