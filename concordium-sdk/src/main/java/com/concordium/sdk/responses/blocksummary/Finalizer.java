package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.responses.AccountIndex;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

/**
 * A finalizer on chain
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public final class Finalizer {
    /**
     * The baker id
     */
    private final AccountIndex bakerId;

    /**
     * The finalizer's weight in the committee.
     */
    private final BigInteger weight;

    /**
     * Whether the finalizer's signature is present.
     */
    private final boolean signed;
}
