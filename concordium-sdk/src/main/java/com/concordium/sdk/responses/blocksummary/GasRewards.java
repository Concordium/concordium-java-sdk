package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class GasRewards {
    private final double chainUpdate;
    private final double accountCreation;
    private final double baker;
    private final double finalizationProof;

    @JsonCreator
    GasRewards(@JsonProperty("chainUpdate") double chainUpdate,
               @JsonProperty("accountCreation") double accountCreation,
               @JsonProperty("baker") double baker,
               @JsonProperty("finalizationProof") double finalizationProof) {
        this.chainUpdate = chainUpdate;
        this.accountCreation = accountCreation;
        this.baker = baker;
        this.finalizationProof = finalizationProof;
    }
}
