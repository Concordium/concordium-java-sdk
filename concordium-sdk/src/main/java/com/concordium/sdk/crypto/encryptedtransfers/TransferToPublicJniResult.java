package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class TransferToPublicJniResult {
    /**
     * A {@link TransferToPublicJniOutput} object, containing the output of the transfer to public function if it succeeded.
     * Populated iff {@link TransferToPublicJniResult#isSuccess} is true.
     */
    @JsonProperty("Ok")
    private final TransferToPublicJniOutput ok;

    /**
     * A {@link JNIError} object, containing an error message if the generate baker keys function failed.
     * Populated iff {@link TransferToPublicJniResult#isSuccess} is false.
     */
    @JsonProperty("Err") //CryptoJniResultCode
    private final JNIError err;

    /**
     * Whether the function succeeded or not.
     */
    private boolean isSuccess;

    @JsonCreator
    TransferToPublicJniResult(
            @JsonProperty("Ok") TransferToPublicJniOutput ok,
            @JsonProperty("Err") JNIError err
    ) {
        this.ok = ok;
        this.err = err;
        if (Objects.isNull(err)) {
            isSuccess = true;
        }
    }

}
