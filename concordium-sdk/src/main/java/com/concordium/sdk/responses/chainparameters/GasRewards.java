package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

/**
 * Distribution of gas rewards for chain parameters v0 and v1.
 */
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class GasRewards {

    /**
     * Fraction paid for including an update transaction in a block.
     */
    @JsonProperty("chainUpdate")
    @Getter
    private final double chainUpdate;

    /**
     * Fraction paid for including each account creation transaction in a block.
     */
    @JsonProperty("accountCreation")
    @Getter
    private final double accountCreation;

    /**
     * The fraction paid to the baker.
     */
    @JsonProperty("baker")
    @Getter
    private final double baker;

    /**
     * Fraction paid for including a finalization proof in a block.
     * Only present in protocol version 6 and onwards.
     */
    @JsonProperty("finalizationProof")
    private final double finalizationProof;

    public Optional<Double> getFinalizationProof() {
        if (this.type == Type.V2) {
            return Optional.empty();
        } else {
            return Optional.of(finalizationProof);
        }
    }

    private final Type type;

    public static GasRewards from(com.concordium.grpc.v2.GasRewards update) {
        GasRewardsBuilder builder = GasRewards
                .builder();
        builder
                .baker(update.getBaker().getPartsPerHundredThousand() / 100_000d)
                .chainUpdate(update.getChainUpdate().getPartsPerHundredThousand() / 100_000d)
                .accountCreation(update.getAccountCreation().getPartsPerHundredThousand() / 100_000d);
        if (update.hasFinalizationProof()) {
            builder.finalizationProof(PartsPerHundredThousand.from(update.getFinalizationProof().getPartsPerHundredThousand()).asDouble());
            builder.type(Type.V1);
        } else {
            builder.type(Type.V2);
        }
        return builder.build();
    }

    /**
     * Types for gas reward parameters.
     * From protocol version 6 the {@link Type#V1} is in effect,
     * and as a consequence the finalization proof no longer exists.
     */
    public enum Type {
        V1, V2
    }
}
