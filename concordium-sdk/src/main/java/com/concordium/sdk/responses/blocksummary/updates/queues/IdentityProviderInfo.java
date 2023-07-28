package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.grpc.v2.IpInfo;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
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
    @Builder
    public IdentityProviderInfo(@JsonProperty("ipIdentity") int ipIdentity,
                                @JsonProperty("ipDescription") Description description,
                                @JsonProperty("ipCdiVerifyKey") ED25519PublicKey ipCdiVerifyKey,
                                @JsonProperty("ipVerifyKey") PSPublicKey ipVerifyKey) {
        this.ipIdentity = UInt32.from(ipIdentity);
        this.description = description;
        this.ipCdiVerifyKey = ipCdiVerifyKey;
        this.ipVerifyKey = ipVerifyKey;
    }

    public static Optional<ImmutableList<IdentityProviderInfo>> fromJsonArray(String jsonValue) {
        try {
            return Optional.ofNullable(JsonMapper.INSTANCE.readValue(jsonValue, IdentityProviderInfo[].class))
                    .map(ImmutableList::copyOf);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Identity Provider Array JSON", e);
        }
    }

    public static IdentityProviderInfo from(IpInfo ip) {
        return IdentityProviderInfo
                .builder()
                .ipIdentity(ip.getIdentity().getValue())
                .description(Description
                        .builder()
                        .name(ip.getDescription().getName())
                        .url(ip.getDescription().getUrl())
                        .description(ip.getDescription().getDescription())
                        .build())
                .ipCdiVerifyKey(ED25519PublicKey.from(ip.getCdiVerifyKey().getValue().toByteArray()))
                .ipVerifyKey(PSPublicKey.from(ip.getVerifyKey().getValue().toByteArray()))
                .build();
    }
}
