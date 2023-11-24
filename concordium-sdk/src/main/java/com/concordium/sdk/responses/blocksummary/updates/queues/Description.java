package com.concordium.sdk.responses.blocksummary.updates.queues;

import lombok.*;

/**
 * A description contains meta information about an {@link IdentityProviderInfo} or {@link AnonymityRevokerInfo}
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
@RequiredArgsConstructor
public class Description {
    private final String name;
    private final String url;
    private final String description;
}
