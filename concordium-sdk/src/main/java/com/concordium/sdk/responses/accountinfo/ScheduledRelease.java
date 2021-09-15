package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class ScheduledRelease {
    private final int releaseTimestamp;
    private final String releaseAmount;
    private final List<String> releaseTransactions;

    @JsonCreator
    ScheduledRelease(@JsonProperty("releaseTimestamp") int releaseTimestamp,
                     @JsonProperty("releaseAmount") String releaseAmount,
                     @JsonProperty("releaseTransactions") List<String> releaseTransactions) {
        this.releaseTimestamp = releaseTimestamp;
        this.releaseAmount = releaseAmount;
        this.releaseTransactions = releaseTransactions;
    }
}
