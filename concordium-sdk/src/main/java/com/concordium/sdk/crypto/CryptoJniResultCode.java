package com.concordium.sdk.crypto;

import lombok.Getter;

public enum CryptoJniResultCode {
    SUCCESS(0),
    JSON_DESERIALIZATION_ERROR(1, "Json Deserialization error."),
    UTF8_ERROR(2, "Incorrect Input String."),
    NATIVE_CONVERSION_ERROR(3, "Native Conversion Error."),
    AMOUNT_DECRYPTION_ERROR(4, "Input Encrypted Amount could not be Decrypted."),
    PAYLOAD_CREATION_ERROR(5, "JNI Output Payload could not be created"),
    ERROR_UNKNOWN_RESULT_CODE(6, "Unknown EncryptedTransfersResultCode");

    private final int code;
    @Getter
    private final String errorMessage;

    CryptoJniResultCode(int code) {
        this.code = code;
        this.errorMessage = "";
    }

    CryptoJniResultCode(int code, String err) {
        this.code = code;
        this.errorMessage = err;
    }
}
