package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class EncryptedAmountTransferJniResult {
    @JsonProperty("Ok")
    private final EncryptedAmountTransferJniOutput ok;

    @JsonProperty("Err")
    private final JNIError err;

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

    public boolean isok() {
        return isSuccess;
    }
}
