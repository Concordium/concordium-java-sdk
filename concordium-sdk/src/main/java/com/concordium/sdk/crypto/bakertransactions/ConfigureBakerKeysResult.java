package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class ConfigureBakerKeysResult {
    /**
     * An optional `ConfigureBakerKeysJniOutput` object, containing the output of the configure baker keys function.
     */
    @JsonProperty("Ok")
    private final Optional<ConfigureBakerKeysJniOutput> ok;

    /**
     * An optional `CryptoJniResultCode` object, containing an error code if the configure baker keys function failed.
     */
    @JsonProperty("Err")
    private final Optional<CryptoJniResultCode> err;

    @JsonCreator
    ConfigureBakerKeysResult(
            @JsonProperty("Ok") ConfigureBakerKeysJniOutput ok,
            @JsonProperty("Err") CryptoJniResultCode err
    ) {
        this.ok = Optional.ofNullable(ok);
        this.err = Optional.ofNullable(err);
    }

    /**
     * Returns a boolean indicating whether the `ok` field is present (i.e. whether the configure baker keys payload was successfully generated).
     *
     * @return a boolean indicating whether the `ok` field is present.
     */
    public boolean isok() {
        return ok.isPresent();
    }
}
