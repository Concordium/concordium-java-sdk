package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * A CCD release in the future.
 */
@Getter
@ToString
public final class ScheduledRelease {

    /**
     * The time when the CCD will be released for the account.
     */
    private final Timestamp releaseTimestamp;
    /**
     * The amount that will be released.
     */
    private final String releaseAmount;
    /**
     * A list of 'origin' transactions for the {@link ScheduledRelease}
     */
    private final List<Hash> releaseTransactions;

    @JsonCreator
    ScheduledRelease(@JsonProperty("timestamp") Timestamp releaseTimestamp,
                     @JsonProperty("amount") String releaseAmount,
                     @JsonProperty("transactions") List<Hash> releaseTransactions) {
        this.releaseTimestamp = releaseTimestamp;
        this.releaseAmount = releaseAmount;
        this.releaseTransactions = releaseTransactions;
    }
}
