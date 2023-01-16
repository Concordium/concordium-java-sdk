package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.crypto.CryptoJniResultCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class TransferToPublicJniResult {
    /**
     * An optional `TransferToPublicJniOutput` object, containing the output of the encrypted transfer function.
     */
    @JsonProperty("Ok")
    private final Optional<TransferToPublicJniOutput> ok;

    /**
     * An optional `CryptoJniResultCode` object, containing an error code if the encrypted transfer function failed.
     */
    @JsonProperty("Err")
    private final Optional<CryptoJniResultCode> err;

    @JsonCreator
    TransferToPublicJniResult(
            @JsonProperty("Ok") TransferToPublicJniOutput ok,
            @JsonProperty("Err") CryptoJniResultCode err
    ) {
        this.ok = Optional.ofNullable(ok);
        this.err = Optional.ofNullable(err);
    }

    /**
     * Returns a boolean indicating whether the `ok` field is present (i.e. whether the encrypted transfer was successful).
     *
     * @return a boolean indicating whether the `ok` field is present.
     */
    public boolean isok() {
        return ok.isPresent();
    }
}
