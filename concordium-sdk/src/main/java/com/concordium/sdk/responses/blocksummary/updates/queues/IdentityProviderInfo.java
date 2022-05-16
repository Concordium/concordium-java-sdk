package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Identity provider info
 */
@Getter
@ToString
@EqualsAndHashCode
public final class IdentityProviderInfo {

    private final UInt32 ipIdentity;
    private final Description description;
    private final byte[] ipCdiVerifyKey;
    private final String ipVerifyKey;

    @JsonCreator
    IdentityProviderInfo(@JsonProperty("ipIdentity") int ipIdentity,
                         @JsonProperty("ipDescription") Description description,
                         @JsonProperty("ipCdiVerifyKey") String ipCdiVerifyKey,
                         @JsonProperty("ipVerifyKey") String ipVerifyKey) {
        this.ipIdentity = UInt32.from(ipIdentity);
        this.description = description;
        this.ipCdiVerifyKey = ED25519PublicKey.from(ipCdiVerifyKey).getBytes();
        this.ipVerifyKey = ipVerifyKey;
    }


}
