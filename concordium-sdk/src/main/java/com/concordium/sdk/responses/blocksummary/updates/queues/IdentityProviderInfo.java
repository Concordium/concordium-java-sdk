package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.grpc.v2.IpInfo;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.types.UInt32;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

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

    @Builder
    @Jacksonized
    public IdentityProviderInfo(int ipIdentity,
                                Description description,
                                ED25519PublicKey ipCdiVerifyKey,
                                PSPublicKey ipVerifyKey) {
        this.ipIdentity = UInt32.from(ipIdentity);
        this.description = description;
        this.ipCdiVerifyKey = ipCdiVerifyKey;
        this.ipVerifyKey = ipVerifyKey;
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
