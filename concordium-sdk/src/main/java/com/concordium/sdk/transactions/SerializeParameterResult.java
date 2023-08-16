package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.JNIError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * TODO comment
 */
@Data
@ToString(doNotUseGetters = true)
public class SerializeParameterResult {

    @JsonProperty("Ok")
    private final String serializedParameter;

    @JsonProperty("Err")
    private final JNIError err;

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
