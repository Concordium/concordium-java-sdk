package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.Optional;

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

    @ToString.Exclude
    private final ElgamalPublicKey anonymityRevokerPublicKey;

    @JsonCreator
    @Builder
    public AnonymityRevokerInfo(@JsonProperty("arIdentity") int arIdentity, @JsonProperty("arDescription") Description description, @JsonProperty("arPublicKey") ElgamalPublicKey arPublicKey) {
        this.arIdentity = UInt32.from(arIdentity);
        this.description = description;
        this.anonymityRevokerPublicKey = arPublicKey;
    }

    public static Optional<ImmutableList<AnonymityRevokerInfo>> fromJsonArray(String jsonValue) {
        try {
            val parsed = JsonMapper.INSTANCE.readValue(jsonValue, AnonymityRevokerInfo[].class);
            return Optional.ofNullable(parsed).map(ImmutableList::copyOf);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Anonymity Revoker JSON", e);
        }
    }

}
