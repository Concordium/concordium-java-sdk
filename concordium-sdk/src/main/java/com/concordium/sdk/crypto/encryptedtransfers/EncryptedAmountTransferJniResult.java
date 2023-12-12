package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class EncryptedAmountTransferJniResult {
    /**
     * A {@link EncryptedAmountTransferJniResult} object, containing the output of the transfer encrypted amount function if it succeeded.
     * Populated iff {@link EncryptedAmountTransferJniResult#isSuccess} is true.
     */
    @JsonProperty("Ok")
    private final EncryptedAmountTransferJniOutput ok;

    /**
     * A {@link JNIError} object, containing an error message if the transfer encrypted amount function failed.
     * Populated iff {@link EncryptedAmountTransferJniResult#isSuccess} is false.
     */
    @JsonProperty("Err")
    private final JNIError err;

    /**
     * Whether the function succeeded or not.
     */
    private boolean isSuccess;

    @JsonCreator
    EncryptedAmountTransferJniResult(
            @JsonProperty("Ok") EncryptedAmountTransferJniOutput ok,
            @JsonProperty("Err") JNIError err
    ) {
        this.ok = ok;
        this.err = err;
        if (Objects.isNull(err)) {
            isSuccess = true;
        }
    }

}
