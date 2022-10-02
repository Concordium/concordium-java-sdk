package com.concordium.sdk.crypto.encryptedtransfers;

import lombok.Getter;

public enum EncryptedTransfersResultCode {
    SUCCESS(0),
    JSON_DESERIALIZATION_ERROR(1, "Json Deserialization error."),
    UTF8_ERROR(2, "Incorrect Input String."),
    NATIVE_CONVERSION_ERROR(3, "Native Conversion Error."),
    AMOUNT_DECRYPTION_ERROR(4, "Input Encrypted Amount could not be Decrypted."),
    PAYLOAD_CREATION_ERROR(5, "JNI Output Payload could not be created"),
    ERROR_UNKNOWN_RESULT_CODE(100, "Unknown EncryptedTransfersResultCode");

    private final int code;
    @Getter
    private final String errorMessage;

    EncryptedTransfersResultCode(int code) {
        this.code = code;
        this.errorMessage = "";
    }

    EncryptedTransfersResultCode(int code, String err) {
        this.code = code;
        this.errorMessage = err;
    }
}
