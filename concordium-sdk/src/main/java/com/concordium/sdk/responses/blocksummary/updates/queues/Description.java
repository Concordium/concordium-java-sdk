package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A description contains meta information about an {@link IdentityProviderInfo} or {@link AnonymityRevokerInfo}
 */
@ToString
@EqualsAndHashCode
@Getter
public class Description {
    private final String name;
    private final String url;
    private final String description;

    @JsonCreator
    @Builder
    public Description(@JsonProperty("name") String name, @JsonProperty("url") String url, @JsonProperty("description") String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }
}
