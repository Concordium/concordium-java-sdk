package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

/**
 * CBOR-serializable content of an on-chain record (RegisterData transaction)
 * anchoring the event that a verification request has been made.
 *
 * @see com.concordium.sdk.serializing.CborMapper
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class VerificationRequestAnchor {

    private final int version;

    /**
     * The hash of presentationContext and credentialStatement.
     */
    @NonNull
    private final Hash hash;

    /**
     * Optional public information.
     */
    @JsonProperty("public")
    private final Object publicInfo;

    public String getType() {
        return TYPE;
    }

    public Optional<Object> getPublicInfo() {
        return Optional.ofNullable(publicInfo);
    }

    public static final String TYPE = "CCDVRA";
    public static final int V1 = 1;
}
