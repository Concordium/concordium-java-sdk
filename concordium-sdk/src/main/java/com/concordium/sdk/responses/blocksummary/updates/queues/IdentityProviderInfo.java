package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

/**
 * Identity provider info
 */
@Getter
@ToString
@EqualsAndHashCode
public final class IdentityProviderInfo {

    private final UInt32 ipIdentity;
    private final Description description;
    private final ED25519PublicKey ipCdiVerifyKey;
    private final PSPublicKey ipVerifyKey;

    @JsonCreator
    public IdentityProviderInfo(@JsonProperty("ipIdentity") int ipIdentity,
                                @JsonProperty("ipDescription") Description description,
                                @JsonProperty("ipCdiVerifyKey") ED25519PublicKey ipCdiVerifyKey,
                                @JsonProperty("ipVerifyKey") PSPublicKey ipVerifyKey) {
        this.ipIdentity = UInt32.from(ipIdentity);
        this.description = description;
        this.ipCdiVerifyKey = ipCdiVerifyKey;
        this.ipVerifyKey = ipVerifyKey;
    }

    public static Optional<ImmutableList<IdentityProviderInfo>> fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            return Optional.ofNullable(JsonMapper.INSTANCE.readValue(res.getValue(), IdentityProviderInfo[].class))
                    .map(array -> ImmutableList.copyOf(array));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Identity Provider Array JSON", e);
        }
    }
}
