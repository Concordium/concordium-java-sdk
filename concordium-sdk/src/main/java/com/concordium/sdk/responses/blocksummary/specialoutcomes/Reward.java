package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A reward either stems from baking or finalization.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class Reward {
    private final AccountAddress address;
    private final CCDAmount amount;

    @JsonCreator
    Reward(@JsonProperty("address") AccountAddress address, @JsonProperty("amount") CCDAmount amount) {
        this.address = address;
        this.amount = amount;
    }
}
