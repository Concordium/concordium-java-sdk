package com.concordium.sdk.transactions;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Jacksonized
public final class AccountNonce {

    /**
     * The nonce that should be used.
     */
    @JsonProperty("nonce")
    private final Nonce nonce;

    /**
     * A flag indicating whether all known transactions are finalized.
     * This can be used as an indicator of how reliable the `nonce` value is.
     */
    @JsonProperty("allFinal")
    private final boolean allFinal;
}
