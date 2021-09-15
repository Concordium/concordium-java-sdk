package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class BlockSummary {
    private final List<Object> transactionSummaries;
    private final List<SpecialEvent> specialEvents;
    private final FinalizationData finalizationData;
    private final Updates updates;

    public static BlockSummary fromJson(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, BlockSummary.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockSummary JSON", e);
        }
    }

    @JsonCreator
    BlockSummary(@JsonProperty("transactionSummaries") List<Object> transactionSummaries,
                 @JsonProperty("specialEvents") List<SpecialEvent> specialEvents,
                 @JsonProperty("finalizationData") FinalizationData finalizationData,
                 @JsonProperty("updates") Updates updates) {
        this.transactionSummaries = transactionSummaries;
        this.specialEvents = specialEvents;
        this.finalizationData = finalizationData;
        this.updates = updates;
    }
}
