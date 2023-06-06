package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Payment for a particular account.
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
public final class PaydayFoundationReward extends SpecialOutcome {

    /**
     * The account that got rewarded.
     */
    private final AccountAddress foundationAccount;

    /**
     * The transaction fee reward at payday to the account.
     */
    private final CCDAmount developmentCharge;

    @JsonCreator
    PaydayFoundationReward(
            @JsonProperty("foundationAccount") AccountAddress foundationAccount,
            @JsonProperty("developmentCharge") CCDAmount developmentCharge) {
        this.foundationAccount = foundationAccount;
        this.developmentCharge = developmentCharge;
    }
}
