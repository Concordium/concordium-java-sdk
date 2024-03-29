package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Minting of new GTU.
 */
@ToString
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public final class Mint extends SpecialOutcome {
    /**
     * The amount allocated to the baking reward account
     */
    private final CCDAmount mintBakingReward;
    /**
     * The amount allocated to the finalization reward account.
     */
    private final CCDAmount mintFinalizationReward;
    /**
     * The amount allocated as the platform development charge.
     */
    private final CCDAmount mintPlatformDevelopmentCharge;
    /**
     * The account to which the platform development charge is paid.
     */
    private final AccountAddress foundationAccount;
}
