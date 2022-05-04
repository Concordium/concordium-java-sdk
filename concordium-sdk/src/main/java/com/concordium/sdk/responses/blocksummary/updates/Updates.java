package com.concordium.sdk.responses.blocksummary.updates;

import com.concordium.sdk.responses.blocksummary.updates.chainparameters.ChainParameters;
import com.concordium.sdk.responses.blocksummary.updates.keys.Keys;
import com.concordium.sdk.responses.blocksummary.updates.queues.UpdateQueues;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class Updates<ChainParameters> {

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
