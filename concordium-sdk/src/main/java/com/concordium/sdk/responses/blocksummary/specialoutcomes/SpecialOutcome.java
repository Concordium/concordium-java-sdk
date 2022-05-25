package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Special outcomes
 *
 * Types must match <a href="https://github.com/Concordium/concordium-base/blob/23c2f4f2fb524e60548f052d0422b567ec977822/haskell-src/Concordium/Types/Transactions.hs#L606">https://github.com/Concordium/concordium-base/blob/23c2f4f2fb524e60548f052d0422b567ec977822/haskell-src/Concordium/Types/Transactions.hs#L606</a>
 *
 * See deriving types for the concrete instances.
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tag")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BakingRewards.class, name = "BakingRewards"),
        @JsonSubTypes.Type(value = Mint.class, name = "Mint"),
        @JsonSubTypes.Type(value = FinalizationRewards.class, name = "FinalizationRewards"),
        @JsonSubTypes.Type(value = BlockReward.class, name = "BlockReward"),
        @JsonSubTypes.Type(value = PaydayFoundationReward.class, name = "PaydayFoundationReward"),
        @JsonSubTypes.Type(value = PaydayAccountReward.class, name = "PaydayAccountReward"),
        @JsonSubTypes.Type(value = BlockAccrueReward.class, name = "BlockAccrueReward"),
        @JsonSubTypes.Type(value = PaydayPoolReward.class, name = "PaydayPoolReward"),
})
public abstract class SpecialOutcome {
}
