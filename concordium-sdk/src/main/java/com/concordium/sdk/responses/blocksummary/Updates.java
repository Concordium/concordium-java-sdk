package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class Updates {
    private final ChainParameters chainParameters;
    private final Keys keys;
    private final UpdateQueues updateQueues;
    private final ProtocolUpdate protocolUpdate;

    @JsonCreator
    Updates(@JsonProperty("chainParameters") ChainParameters chainParameters,
            @JsonProperty("keys") Keys keys,
            @JsonProperty("updateQueues") UpdateQueues updateQueues,
            @JsonProperty("protocolUpdate") ProtocolUpdate protocolUpdate) {
        this.chainParameters = chainParameters;
        this.keys = keys;
        this.updateQueues = updateQueues;
        this.protocolUpdate = protocolUpdate;
    }
}
