package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class ScheduledRelease {
    private final double releaseTimestamp;
    private final String releaseAmount;
    private final List<Hash> releaseTransactions;

    @JsonCreator
    ScheduledRelease(@JsonProperty("timestamp") double releaseTimestamp,
                     @JsonProperty("amount") String releaseAmount,
                     @JsonProperty("transactions") List<Hash> releaseTransactions) {
        this.releaseTimestamp = releaseTimestamp;
        this.releaseAmount = releaseAmount;
        this.releaseTransactions = releaseTransactions;
    }
}
