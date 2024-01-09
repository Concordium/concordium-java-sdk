package com.concordium.sdk.responses.blocksummary.updates.queues;

import lombok.*;

/**
 * A description contains meta information about an {@link IdentityProviderInfo}
 * or {@link AnonymityRevokerInfo}
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Description {
    private String name;
    private String url;
    private String description;
}
