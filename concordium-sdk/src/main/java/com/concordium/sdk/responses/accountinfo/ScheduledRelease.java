package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;

import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class ScheduledRelease {
    private final Timestamp timestamp;
    private final CCDAmount amount;
    private final List<Hash> transactions;

    @JsonCreator
    ScheduledRelease(@JsonProperty("timestamp") Timestamp timestamp,
                     @JsonProperty("amount") CCDAmount amount,
                     @JsonProperty("transactions") List<Hash> transactions) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.transactions = transactions;

    }
}
