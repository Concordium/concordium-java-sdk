package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.grpc.v2.ArInfo;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.types.UInt32;
import lombok.*;

/**
 * Anonymity revoker info
 */
@Getter
@ToString
@EqualsAndHashCode
public final class AnonymityRevokerInfo {
    /**
     * The anonymity revoker id
     */
    private final UInt32 arIdentity;

    /**
     * A description of the anonymity revoker
     */
    private final Description description;

    private final ElgamalPublicKey anonymityRevokerPublicKey;
    @Builder
    public AnonymityRevokerInfo(int arIdentity, Description description, ElgamalPublicKey arPublicKey) {
        this.arIdentity = UInt32.from(arIdentity);
        this.description = description;
        this.anonymityRevokerPublicKey = arPublicKey;
    }

    public static AnonymityRevokerInfo from(ArInfo ar) {
        return AnonymityRevokerInfo
                .builder()
                .arIdentity(ar.getIdentity().getValue())
                .description(Description
                        .builder()
                        .url(ar.getDescription().getUrl())
                        .description(ar.getDescription().getDescription())
                        .name(ar.getDescription().getName())
                        .build())
                .arPublicKey(ElgamalPublicKey.from(ar.getPublicKey().getValue().toByteArray()))
                .build();
    }
}
