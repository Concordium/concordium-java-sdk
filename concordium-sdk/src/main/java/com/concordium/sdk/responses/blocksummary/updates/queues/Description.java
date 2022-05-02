package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class Description {
    private final String name;
    private final String url;
    private final String description;

    @JsonCreator
    Description(@JsonProperty("name") String name, @JsonProperty("url") String url, @JsonProperty("description") String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }
}
