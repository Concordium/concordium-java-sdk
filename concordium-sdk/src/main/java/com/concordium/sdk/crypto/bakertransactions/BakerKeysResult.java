package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

/**
 * Class that holds the result of generating baker keys payload
 */
@Data
public class BakerKeysResult {
    /**
     * An optional `BakerKeysJniOutput` object, containing the output of the generate baker keys function.
     */
    @JsonProperty("Ok")
    private final Optional<BakerKeysJniOutput> ok;

    /**
     * An optional `CryptoJniResultCode` object, containing an error code if the generate baker keys function failed.
     */
    @JsonProperty("Err")
    private final Optional<CryptoJniResultCode> err;

    @JsonCreator
    BakerKeysResult(
            @JsonProperty("Ok") BakerKeysJniOutput ok,
            @JsonProperty("Err") CryptoJniResultCode err
    ) {
        this.ok = Optional.ofNullable(ok);
        this.err = Optional.ofNullable(err);
    }

    /**
     * Returns a boolean indicating whether the `ok` field is present (i.e. whether the baker key was successfully generated).
     *
     * @return a boolean indicating whether the `ok` field is present.
     */
    public boolean isok() {
        return ok.isPresent();
    }
}
