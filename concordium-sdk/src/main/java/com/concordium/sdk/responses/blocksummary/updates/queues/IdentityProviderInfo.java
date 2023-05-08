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

    /**
     * Unique identifier of the identity provider.
     */
    private final UInt32 ipIdentity;
    /**
     * Description of the identity provider.
     */
    private final Description description;
    /**
     * ED25519 public key of the identity provider.
     */
    private final ED25519PublicKey ipCdiVerifyKey;
    /**
     * Pointcheval-Sanders public key of the identity provider.
     */
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

    /**
     * Parses {@link IpInfo} to {@link IdentityProviderInfo}.
     * @param ipInfo {@link IpInfo} returned by the GRPC C2 API.
     * @return parsed {@link IdentityProviderInfo}.
     */
    public static IdentityProviderInfo parse(IpInfo ipInfo) {
        return IdentityProviderInfo.builder()
                .ipIdentity(ipInfo.getIdentity().getValue())
                .description(Description.parse(ipInfo.getDescription()))
                .ipCdiVerifyKey(ED25519PublicKey.from(ipInfo.getCdiVerifyKey().getValue().toByteArray()))
                .ipVerifyKey(PSPublicKey.from(ipInfo.getVerifyKey().getValue().toByteArray()))
                .build();
    }

}
