package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@ToString(doNotUseGetters = true)
public class ConfigureBakerKeysResult {
    /**
     * A {@link ConfigureBakerKeysResult} object, containing the output of the configure baker keys function if it succeeded.
     * Populated iff {@link ConfigureBakerKeysResult#isSuccess} is true.
     */
    @JsonProperty("Ok")
    private final ConfigureBakerKeysJniOutput ok;

    /**
     * A {@link JNIError} object, containing an error message if the configure baker keys function failed.
     * Populated iff {@link ConfigureBakerKeysResult#isSuccess} is false.
     */
    @JsonProperty("Err")
    private final JNIError err;

    /**
     * Whether the function succeeded or not.
     */
    private boolean isSuccess;

    @JsonCreator
    ConfigureBakerKeysResult(
            @JsonProperty("Ok") ConfigureBakerKeysJniOutput ok,
            @JsonProperty("Err") JNIError err
    ) {
        this.ok = ok;
        this.err = err;
        if (Objects.isNull(err)) {
            isSuccess = true;
        }
    }

}
