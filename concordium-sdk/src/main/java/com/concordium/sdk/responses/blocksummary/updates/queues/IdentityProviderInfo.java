package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Identity provider info
 *
 * Note. the current implementation does not support the public key of the identity provider.
 *
 */
@Getter
@ToString
@EqualsAndHashCode
public final class IdentityProviderInfo {

    private final int ipIdentity;
    private final Description description;
    private final byte[] ipCdiVerifyKey;

    @JsonCreator
    IdentityProviderInfo(@JsonProperty("ipIdentity") int ipIdentity,
                         @JsonProperty("description") Description description,
                         @JsonProperty("ipCdiVerifyKey") String ipCdiVerifyKey) {
        this.ipIdentity = ipIdentity;
        this.description = description;
        this.ipCdiVerifyKey = ED25519PublicKey.from(ipCdiVerifyKey).getBytes();
    }


}