package com.concordium.sdk.exceptions;

import com.concordium.sdk.crypto.encryptedtransfers.EncryptedTransfersResultCode;
import lombok.Getter;

public final class EncryptedTransfersException extends RuntimeException {
    @Getter
    private final EncryptedTransfersResultCode code;

    private EncryptedTransfersException(EncryptedTransfersResultCode code) {
        super(code.getErrorMessage());
        this.code = code;
    }

    public static EncryptedTransfersException from(EncryptedTransfersResultCode code) {
        return new EncryptedTransfersException(code);
    }
}
