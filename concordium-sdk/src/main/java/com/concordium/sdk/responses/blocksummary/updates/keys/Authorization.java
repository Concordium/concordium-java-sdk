package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.concordium.grpc.v2.AccessStructure;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * Level 2 key authorizations.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public final class Authorization {
    /**
     * The number of keys required to authorize an update.
     */
    private final UInt32 threshold;

    /**
     * The indices of the authorized keys.
     * See {@link Level2KeysUpdates#getVerificationKeys()} for the list of keys which these indices serve as pointers.
     */
    private final List<Integer> authorizedKeys;

    @JsonCreator
    Authorization(@JsonProperty("threshold") byte threshold, @JsonProperty("authorizedKeys") List<Integer> authorizedKeys) {
        this.threshold = UInt32.from(threshold);
        this.authorizedKeys = authorizedKeys;
    }

    /**
     * Parses {@link AccessStructure} to {@link Authorization}.
     * @param accessStructure {@link AccessStructure} returned by the GRPC V2 API.
     * @return parsed {@link Authorization}.
     */
    public static Authorization parse(AccessStructure accessStructure) {
        val authorizedKeys = new ImmutableList.Builder<Integer>();
        accessStructure.getAccessPublicKeysList().forEach(i -> authorizedKeys.add(i.getValue()));
        return Authorization.builder()
                .authorizedKeys(authorizedKeys.build())
                .threshold(UInt32.from(accessStructure.getAccessThreshold().getValue())) // I believe this is wrong
                .build();
    }
}
