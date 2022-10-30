package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class AddBakerKeysResult {
    @JsonProperty("Ok")
    private final Optional<AddBakerKeysJniOutput> ok;

    @JsonProperty("Err")
    private final Optional<CryptoJniResultCode> err;

    @JsonCreator
    AddBakerKeysResult(
            @JsonProperty("Ok") AddBakerKeysJniOutput ok,
            @JsonProperty("Err") CryptoJniResultCode err
    ) {
        this.ok = Optional.ofNullable(ok);
        this.err = Optional.ofNullable(err);
    }

    public boolean isok() {
        return ok.isPresent();
    }
}
