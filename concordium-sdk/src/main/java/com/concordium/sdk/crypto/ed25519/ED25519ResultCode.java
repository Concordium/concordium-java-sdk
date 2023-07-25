package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.Getter;

public enum ED25519ResultCode {
    SUCCESS(0),
    NATIVE_CONVERSION_ERROR(1, "Unable to do native conversion."),
    MALFORMED_SECRET_KEY(2, "Invalid ED25519SecretKey"),
    MALFORMED_PUBLIC_KEY(3, "Invalid ED25519PublicKey"),
    SIGNATURE_VERIFICATION_FAILURE(4, "Signature failed verification"),
    SECRET_KEY_GENERATION_FAILURE(5, "Failed creating secret key"),
    PUBLIC_KEY_GENERATION_FAILURE(6, "Failed creating public key"),
    ERROR_UNKNOWN_RESULT_CODE(7, "Unknown ED25519ResultCode");

    private final int code;
    @Getter
    private final String errorMessage;

    ED25519ResultCode(int code) {
        this.code = code;
        this.errorMessage = "";
    }

    ED25519ResultCode(int code, String err) {
        this.code = code;
        this.errorMessage = err;
    }

    /**
     * Creates {@link ED25519ResultCode} from input code.
     *
     * @param code int value of {@link ED25519ResultCode}.
     * @return Created {@link ED25519ResultCode}.
     * @throws ED25519Exception If the input is out of range.
     */
    static ED25519ResultCode from(int code) {
        for (ED25519ResultCode value : ED25519ResultCode.values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw ED25519Exception.from(ERROR_UNKNOWN_RESULT_CODE);
    }

    boolean failed() {
        return this != SUCCESS;
    }

}
