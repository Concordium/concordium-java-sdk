package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.responses.blocksummary.specialoutcomes.BakingRewards;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.Mint;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.SpecialOutcome;
import com.concordium.sdk.responses.blocksummary.updates.Updates;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.ChainParameters;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.ChainParametersV0;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.ChainParametersV1;
import com.concordium.sdk.responses.transactionstatus.TransactionSummary;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class BlockSummary {
    /**
     * The protocol version of the chain at the block.
     */
    private final int protocolVersion;
    /**
     * The list of transaction summaries for the block.
     */
    private final List<TransactionSummary> transactionSummaries;

    /**
     * List of special transaction outcomes.
     */
    private final List<SpecialOutcome> specialOutcomes;

    /**
     * Finalization data for the block.
     */
    private final FinalizationData finalizationData;

    /**
     * Chain updates
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, defaultImpl = ChainParametersV0.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ChainParametersV1.class)
    })
    private final Updates<ChainParameters> updates;

    public static BlockSummary fromJson(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, BlockSummary.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockSummary JSON", e);
        }
    }

    @JsonCreator
    BlockSummary(@JsonProperty("protocolVersion") int protocolVersion,
                 @JsonProperty("transactionSummaries") List<TransactionSummary> transactionSummaries,
                 @JsonProperty("specialEvents") List<SpecialOutcome> specialOutcomes,
                 @JsonProperty("finalizationData") FinalizationData finalizationData,
                 @JsonProperty("updates") Updates updates) {
        this.protocolVersion = protocolVersion;
        this.transactionSummaries = transactionSummaries;
        this.specialOutcomes = specialOutcomes;
        this.finalizationData = finalizationData;
        this.updates = updates;
    }
}
