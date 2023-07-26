package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.SpecialOutcome;
import com.concordium.sdk.responses.blocksummary.updates.Updates;
import com.concordium.sdk.responses.transactionstatus.TransactionSummary;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
public final class BlockSummary {
    /**
     * The protocol version of the chain at the block.
     */
    private final ProtocolVersion protocolVersion;
    /**
     * The list of transaction summaries for the block.
     */
    private final ArrayList<TransactionSummary> transactionSummaries;

    /**
     * List of special transaction outcomes.
     */
    private final ArrayList<SpecialOutcome> specialOutcomes;

    /**
     * Finalization data for the block.
     */
    private final FinalizationData finalizationData;

    /**
     * Chain updates
     */
    private final Updates updates;

    public static BlockSummary fromJson(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, BlockSummary.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockSummary JSON", e);
        }
    }

    @JsonCreator
    BlockSummary(@JsonProperty("protocolVersion") ProtocolVersion protocolVersion,
                 @JsonProperty("transactionSummaries") ArrayList<TransactionSummary> transactionSummaries,
                 @JsonProperty("specialEvents") ArrayList<SpecialOutcome> specialOutcomes,
                 @JsonProperty("finalizationData") FinalizationData finalizationData,
                 @JsonProperty("updates") Updates updates) {
        this.protocolVersion = protocolVersion;
        this.transactionSummaries = transactionSummaries;
        this.specialOutcomes = specialOutcomes;
        this.finalizationData = finalizationData;
        this.updates = updates;
    }
}
