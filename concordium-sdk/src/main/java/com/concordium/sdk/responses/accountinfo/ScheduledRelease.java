package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.GTUAmount;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@ToString
public final class ScheduledRelease {
    private final OffsetDateTime timestamp;
    private final GTUAmount amount;
    private final List<Hash> transactions;

    @JsonCreator
    ScheduledRelease(@JsonProperty("timestamp") OffsetDateTime timestamp,
                     @JsonProperty("amount") GTUAmount amount,
                     @JsonProperty("transactions") List<Hash> transactions) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.transactions = transactions;
    }
}
