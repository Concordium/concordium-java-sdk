package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * A description contains meta information about an {@link IdentityProviderInfo} or {@link AnonymityRevokerInfo}
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
@Jacksonized
@RequiredArgsConstructor
public class Description {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("url")
    private final String url;
    @JsonProperty("description")
    private final String description;
}
