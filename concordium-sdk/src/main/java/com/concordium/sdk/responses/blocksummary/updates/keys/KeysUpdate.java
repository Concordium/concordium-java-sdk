package com.concordium.sdk.responses.blocksummary.updates.keys;

import lombok.*;

import java.util.List;

/**
 * Root update & Level 1 update keys.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class KeysUpdate {
    @Singular
    private final List<VerificationKey> verificationKeys;
    private final int threshold;
}
