package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class TransferToPublicJniResult {
    /**
     * An optional `TransferToPublicJniOutput` object, containing the output of the encrypted transfer function.
     */
    @JsonProperty("Ok")
    private final TransferToPublicJniOutput ok;

    /**
     * An optional `CryptoJniResultCode` object, containing an error code if the encrypted transfer function failed.
     */
    @JsonProperty("Err") //CryptoJniResultCode
    private final JNIError err;

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

    /**
     * Returns a boolean indicating whether the `ok` field is present (i.e. whether the encrypted transfer was successful).
     *
     * @return a boolean indicating whether the `ok` field is present.
     */
    public boolean isok() {
        return this.isSuccess;
    }
}
