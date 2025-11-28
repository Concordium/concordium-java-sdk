package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.beans.Transient;
import java.util.Optional;

/**
 * CBOR-serializable content of an on-chain record (RegisterData transaction)
 * anchoring the event that a verification request has been made.
 * <p>
 * For a {@link VerificationRequestV1}, this transaction can be fetched from a node
 * by {@link VerificationRequestV1#getTransactionRef()}
 *
 * @see com.concordium.sdk.serializing.CborMapper
 * @see com.concordium.sdk.ClientV2#getBlockItemStatus(Hash) Fetch a transaction by hash
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class VerificationRequestAnchor {
    private final String type = TYPE;

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

    @Transient
    public Optional<Object> getPublicInfo() {
        return Optional.ofNullable(publicInfo);
    }

    public static final String TYPE = "CCDVRA";
    public static final int V1 = 1;
}
