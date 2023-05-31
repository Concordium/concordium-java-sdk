package com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
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

    /**
     * TODO fix this
     * Parses {@link com.concordium.grpc.v2.GasRewards} to {@link GasRewards}.
     * param gasRewards {@link com.concordium.grpc.v2.GasRewards} returned by the GRPC V2 API.
     * @return parsed {@link GasRewards}.

    public static GasRewards parse(com.concordium.grpc.v2.GasRewards gasRewards) {
        return GasRewards.builder()
                .baker(Fraction.from(gasRewards.getBaker()))
                .finalizationProof(Fraction.from(gasRewards.getFinalizationProof()))
                .accountCreation(Fraction.from(gasRewards.getAccountCreation()))
                .chainUpdate(Fraction.from(gasRewards.getChainUpdate()))
                .build();
    }


    @Override
    public UpdateType getType() {
        return UpdateType.GAS_REWARDS_UPDATE;
    }
     */
}
