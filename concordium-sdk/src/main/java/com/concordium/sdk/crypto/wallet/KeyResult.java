package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@ToString(doNotUseGetters = true)
public class KeyResult {
    
    @JsonProperty("Ok")
    private final String ok;

    @JsonProperty("Err")
    private final JNIError err;

    /**
     * Whether the function succeeded or not.
     */
    private boolean isSuccess;

    @JsonCreator
    KeyResult(
            @JsonProperty("Ok") String ok,
            @JsonProperty("Err") JNIError err
    ) {
        this.ok = ok;
        this.err = err;
        if (Objects.isNull(err)) {
            isSuccess = true;
        }
    }

}
