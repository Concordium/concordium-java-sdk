package com.concordium.sdk.responses.blocksummary.updates.queues;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * A description contains meta information about an {@link IdentityProviderInfo}
 * or {@link AnonymityRevokerInfo}
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
@Jacksonized
public class Description {
    private final String name;
    private final String url;
    private final String description;
}
