package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
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
import lombok.val;

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

    /**
     * Elgamal encryption key of the anonymity revoker.
     */
    private final ElgamalPublicKey anonymityRevokerPublicKey;

    @JsonCreator
    AnonymityRevokerInfo(
            @JsonProperty("arIdentity") int arIdentity,
            @JsonProperty("arDescription") Description description,
            @JsonProperty("arPublicKey") String arPublicKey) {
        this.arIdentity = UInt32.from(arIdentity);
        this.description = description;
        this.anonymityRevokerPublicKey = ElgamalPublicKey.from(arPublicKey);
    }

    public static Optional<ImmutableList<AnonymityRevokerInfo>> fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val array = JsonMapper.INSTANCE.readValue(res.getValue(), AnonymityRevokerInfo[].class);

            return Optional.ofNullable(array).map(arr -> ImmutableList.copyOf(arr));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Anonymity Revoker JSON", e);
        }
    }
}
