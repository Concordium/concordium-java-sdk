package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class EncryptedAmountTransferJniResult {
    @JsonProperty("Ok")
    private final Optional<EncryptedAmountTransferJniOutput> ok;

    @JsonProperty("Err")
    private final Optional<CryptoJniResultCode> err;

    @JsonCreator
    EncryptedAmountTransferJniResult(
            @JsonProperty("Ok") EncryptedAmountTransferJniOutput ok,
            @JsonProperty("Err") CryptoJniResultCode err
    ) {
        this.ok = Optional.ofNullable(ok);
        this.err = Optional.ofNullable(err);
    }

    public boolean isok() {
        return ok.isPresent();
    }
}
