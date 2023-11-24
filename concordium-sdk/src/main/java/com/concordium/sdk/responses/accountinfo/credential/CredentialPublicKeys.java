package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.transactions.Index;
import com.google.common.collect.ImmutableMap;
import lombok.*;

/**
 * Public keys of the {@link Credential}.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CredentialPublicKeys {
    @Singular
    private final ImmutableMap<Index, Key> keys;
    private final int threshold;
}
