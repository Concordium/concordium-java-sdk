package com.concordium.sdk.exceptions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import lombok.Getter;

public final class EncryptedTransfersException extends RuntimeException {
    @Getter
    private final CryptoJniResultCode code;

    private EncryptedTransfersException(CryptoJniResultCode code) {
        super(code.getErrorMessage());
        this.code = code;
    }

    public static EncryptedTransfersException from(CryptoJniResultCode code) {
        return new EncryptedTransfersException(code);
    }
}
