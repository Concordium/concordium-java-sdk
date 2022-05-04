package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Anonymity revoker info
 *
 * Note. the current implementation does not support the public key of the anonymity revoker.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class AnonymityRevokerInfo {
    /**
     * The anonymity revoker id
     */
    private final int arIdentity;

    /**
     * A description of the anonymity revoker
     */
    private final Description description;

    @JsonCreator
    AnonymityRevokerInfo(@JsonProperty("arIdentity") int arIdentity, @JsonProperty("description") Description description) {
        this.arIdentity = arIdentity;
        this.description = description;
    }

}
