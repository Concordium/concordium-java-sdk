package com.concordium.sdk.crypto.encryptedtransfers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class EncryptedTransfersResult {
    @JsonProperty("Ok")
    private final Optional<TransferToPublicJniOutput> ok;

    @JsonProperty("Err")
    private final Optional<EncryptedTransfersResultCode> err;

    @JsonCreator
    EncryptedTransfersResult(
            @JsonProperty("Ok") TransferToPublicJniOutput ok,
            @JsonProperty("Err") EncryptedTransfersResultCode err
    ) {
        this.ok = Optional.ofNullable(ok);
        this.err = Optional.ofNullable(err);
    }

    public boolean isok() {
        return ok.isPresent();
    }
}
