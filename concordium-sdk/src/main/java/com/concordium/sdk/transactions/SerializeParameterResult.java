package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * Class that holds the result of serializing a parameter using the JNI.
 */
@Data
@ToString(doNotUseGetters = true)
public class SerializeParameterResult {

    /**
     * The serialized parameter as hex encoded bytes if the serialize parameter function succeeded.
     * Populated iff {@link SerializeParameterResult#isSuccess} is true.
     */
    @JsonProperty("Ok")
    private final String serializedParameter;

    /**
     * A {@link JNIError} object, containing an error message if the serialize parameter function failed.
     * Populated iff {@link SerializeParameterResult#isSuccess} is false.
     */
    @JsonProperty("Err")
    private final JNIError err;

    /**
     * Whether the function succeeded or not.
     */
    private boolean isSuccess;

    @JsonCreator
    SerializeParameterResult(
            @JsonProperty("Ok") String ok,
            @JsonProperty("Err") JNIError err
    ) {
        this.serializedParameter = ok;
        this.err = err;
        if (Objects.isNull(err)) {
            isSuccess = true;
        }
    }

}
