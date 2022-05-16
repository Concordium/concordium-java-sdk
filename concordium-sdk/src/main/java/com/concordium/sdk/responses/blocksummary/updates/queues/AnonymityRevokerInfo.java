package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    private final String anonymityRevokerPublicKey;

    @JsonCreator
    AnonymityRevokerInfo(@JsonProperty("arIdentity") int arIdentity, @JsonProperty("arDescription") Description description, @JsonProperty("arPublicKey") String arPublicKey) {
        this.arIdentity = UInt32.from(arIdentity);
        this.description = description;
        this.anonymityRevokerPublicKey = arPublicKey;
    }

}
