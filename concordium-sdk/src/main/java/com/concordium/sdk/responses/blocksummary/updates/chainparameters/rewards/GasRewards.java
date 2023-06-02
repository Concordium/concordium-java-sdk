package com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class GasRewards {
    @JsonProperty("chainUpdate")
    private final double chainUpdate;
    @JsonProperty("accountCreation")
    private final double accountCreation;
    @JsonProperty("baker")
    private final double baker;
    @JsonProperty("finalizationProof")
    private final double finalizationProof;
}
