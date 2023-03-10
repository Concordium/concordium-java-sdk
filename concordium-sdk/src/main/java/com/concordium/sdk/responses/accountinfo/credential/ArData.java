package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * Anonymity revoker data
 */
@Getter
@ToString
@Builder
@Jacksonized
@RequiredArgsConstructor
@EqualsAndHashCode
public final class ArData {

    /**
     * Share of the encryption of IdCredPub.
     */
    @JsonProperty("encIdCredPubShare")
    private final EncIdPubShare encIdCredPubShare;
}
