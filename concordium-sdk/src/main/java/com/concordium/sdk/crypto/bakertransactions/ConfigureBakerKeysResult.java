package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class ConfigureBakerKeysResult {
    @JsonProperty("Ok")
    private final Optional<ConfigureBakerKeysJniOutput> ok;

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

    public boolean isok() {
        return ok.isPresent();
    }
}
