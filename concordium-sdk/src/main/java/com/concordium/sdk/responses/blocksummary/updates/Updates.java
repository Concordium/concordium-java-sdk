package com.concordium.sdk.responses.blocksummary.updates;

import com.concordium.sdk.responses.blocksummary.updates.keys.Keys;
import com.concordium.sdk.responses.blocksummary.updates.queues.UpdateQueues;
import com.concordium.sdk.responses.chainparameters.ChainParameters;
import com.concordium.sdk.responses.chainparameters.ChainParametersV0;
import com.concordium.sdk.responses.chainparameters.ChainParametersV1;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class Updates {

    /**
     * The current chain parameters.
     */

    private final ChainParameters chainParameters;

    /**
     * Keys which are authorized for updating the chain parameters.
     */
    private final Keys keys;

    /**
     * Updates enqueued.
     */
    private final UpdateQueues updateQueues;

    /**
     * A protocol Update.
     */
    private final ProtocolUpdate protocolUpdate;

    @JsonCreator
    Updates(@JsonProperty("chainParameters") JsonNode chainParameters,
            @JsonProperty("keys") Keys keys,
            @JsonProperty("updateQueues") UpdateQueues updateQueues,
            @JsonProperty("protocolUpdate") ProtocolUpdate protocolUpdate) {
        if (chainParameters.findPath("minimumThresholdForBaking").isTextual()) {
            this.chainParameters = JsonMapper.INSTANCE.convertValue(chainParameters, ChainParametersV0.class);
        } else {
            this.chainParameters = JsonMapper.INSTANCE.convertValue(chainParameters, ChainParametersV1.class);
        }
        this.keys = keys;
        this.updateQueues = updateQueues;
        this.protocolUpdate = protocolUpdate;
    }
}
