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
     * An optional `BakerKeysJniOutput` object, containing the output of the generate baker keys function.
     */
    @JsonProperty("Ok")
    private final BakerKeysJniOutput ok;

    /**
     * An optional `CryptoJniResultCode` object, containing an error code if the generate baker keys function failed.
     */
    @JsonProperty("Err")
    private final JNIError err;

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

    /**
     * Returns a boolean indicating whether the `ok` field is present (i.e. whether the baker key was successfully generated).
     *
     * @return a boolean indicating whether the `ok` field is present.
     */
    public boolean isok() {
        return this.isSuccess;
    }
}
