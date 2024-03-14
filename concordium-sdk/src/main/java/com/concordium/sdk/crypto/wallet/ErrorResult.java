package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResult extends StringResult {

    @JsonCreator
    ErrorResult(
            @JsonProperty("Ok") String ok,
            @JsonProperty("Err") JNIError err
    ) {
        super(ok, err);
    }
}
