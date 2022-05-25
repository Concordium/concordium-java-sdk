package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * A CCD release in the future.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class ScheduledRelease {

    /**
     * The time when the CCD will be released for the account.
     */
    private final Timestamp timestamp;

    /**
     * The amount that will be released.
     */
    private final CCDAmount amount;

    /**
     * A list of 'origin' transactions for the {@link ScheduledRelease}
     */
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
