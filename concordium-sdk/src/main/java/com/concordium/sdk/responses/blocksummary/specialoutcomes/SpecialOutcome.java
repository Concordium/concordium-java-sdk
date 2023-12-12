package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Special outcomes
 * <p>
 * Types must match <a href="https://github.com/Concordium/concordium-base/blob/23c2f4f2fb524e60548f052d0422b567ec977822/haskell-src/Concordium/Types/Transactions.hs#L606">https://github.com/Concordium/concordium-base/blob/23c2f4f2fb524e60548f052d0422b567ec977822/haskell-src/Concordium/Types/Transactions.hs#L606</a>
 * <p>
 * See deriving types for the concrete instances.
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class SpecialOutcome {
}
