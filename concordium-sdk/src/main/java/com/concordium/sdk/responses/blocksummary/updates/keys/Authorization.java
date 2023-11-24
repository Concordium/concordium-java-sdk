package com.concordium.sdk.responses.blocksummary.updates.keys;

import lombok.*;

import java.util.List;

/**
 * Level 2 key authorizations.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class Authorization {
    /**
     * The threshold
     */
    private final byte threshold;

    /**
     * The indices of the authorized keys.
     * See {@link Level2KeysUpdates#getVerificationKeys()} for the list of keys which these indices serve as pointers.
     */
    @Singular
    private final List<Integer> authorizedKeys;
}
