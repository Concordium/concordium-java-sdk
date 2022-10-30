package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class EncryptedTransfersResult {
    @JsonProperty("Ok")
    private final Optional<TransferToPublicJniOutput> ok;

    @JsonProperty("Err")
    private final Optional<CryptoJniResultCode> err;

    @JsonCreator
    EncryptedTransfersResult(
            @JsonProperty("Ok") TransferToPublicJniOutput ok,
            @JsonProperty("Err") CryptoJniResultCode err
    ) {
        this.ok = Optional.ofNullable(ok);
        this.err = Optional.ofNullable(err);
    }

    public boolean isok() {
        return ok.isPresent();
    }
}
