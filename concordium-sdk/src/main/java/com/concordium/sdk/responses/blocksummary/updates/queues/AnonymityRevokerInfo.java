package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class AnonymityRevokerInfo {
    private final int arIdentity;
    private final Description description;

    //todo: handle the elgamal public key in some way.

    @JsonCreator
    AnonymityRevokerInfo(@JsonProperty("arIdentity") int arIdentity, @JsonProperty("description") Description description) {
        this.arIdentity = arIdentity;
        this.description = description;
    }

}
