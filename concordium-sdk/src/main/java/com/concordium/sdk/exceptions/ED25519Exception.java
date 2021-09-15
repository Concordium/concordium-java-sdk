package com.concordium.sdk.exceptions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import lombok.Getter;

public final class ED25519Exception extends Exception {
    @Getter
    private final ED25519ResultCode code;

    private ED25519Exception(ED25519ResultCode code) {
        super(code.getErrorMessage());
        this.code = code;
    }

    public static ED25519Exception from(ED25519ResultCode code) {
        return new ED25519Exception(code);
    }
}
