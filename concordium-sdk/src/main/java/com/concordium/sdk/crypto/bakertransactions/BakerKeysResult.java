package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * Class that holds the result of generating baker keys payload
 */
@Data
@ToString(doNotUseGetters = true)
public class BakerKeysResult {
    /**
     * A {@link BakerKeysJniOutput} object, containing the output of the generate baker keys function if it succeeded.
     * Populated iff {@link BakerKeysResult#isSuccess} is true.
     */
    @JsonProperty("Ok")
    private final BakerKeysJniOutput ok;

    /**
     * A {@link JNIError} object, containing an error message if the generate baker keys function failed.
     * Populated iff {@link BakerKeysResult#isSuccess} is false.
     */
    @JsonProperty("Err")
    private final JNIError err;

    /**
     * Whether the function succeeded or not.
     */
    private boolean isSuccess;

    @JsonCreator
    BakerKeysResult(
            @JsonProperty("Ok") BakerKeysJniOutput ok,
            @JsonProperty("Err") JNIError err
    ) {
        this.ok = ok;
        this.err = err;
        if (Objects.isNull(err)) {
            isSuccess = true;
        }
    }

}
