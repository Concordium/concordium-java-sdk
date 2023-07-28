package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A reward either stems from baking, including transactions in a block or finalization.
 */
@Getter
@ToString
@Builder
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
