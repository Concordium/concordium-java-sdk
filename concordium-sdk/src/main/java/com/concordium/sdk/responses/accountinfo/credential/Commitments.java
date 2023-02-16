package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.copyOf;

/**
 * Commitments that are part of a normal credential.
 */
@Getter
@ToString
@Builder
@Jacksonized
@EqualsAndHashCode
public final class Commitments {

    /**
     * Commitment to the PRF key.
     */
    @JsonProperty("cmmPrf")
    private final Commitment cmmPrf;

    /**
     * Commitment to the counter used to generate the credential registration id.
     */
    @JsonProperty("cmmCredCounter")
    private final Commitment cmmCredCounter;

    @JsonProperty("cmmIdCredSecSharingCoeff")
    @Singular(value = "cmmIdCredSecSharingCoeffItem")
    private final List<Commitment> cmmIdCredSecSharingCoeff;

    /**
     * Immutable List of commitments to the coefficients of the sharing polynomial. This
     * polynomial is used in a shared encryption of `id_cred_pub` among the
     * anonymity revokers.
     */
    public ImmutableList<Commitment> getCmmIdCredSecSharingCoeff() {
        return copyOf(cmmIdCredSecSharingCoeff);
    }

    /**
     * Commitments to the attributes which have not been revealed in the policy.
     */
    @JsonProperty("cmmAttributes")
    @Singular
    private final Map<AttributeType, Commitment> cmmAttributes;

    /**
     * Commitments to the attributes which have not been revealed in the policy.
     */
    public ImmutableMap<AttributeType, Commitment> getCmmAttributes() {
        return ImmutableMap.copyOf(cmmAttributes);
    }

    /**
     * Commitment to the `max_accounts` value, which determines the maximum number
     * of credentials that may be created from the identity object.
     */
    @JsonProperty("cmmMaxAccounts")
    private final Commitment cmmMaxAccounts;
}
