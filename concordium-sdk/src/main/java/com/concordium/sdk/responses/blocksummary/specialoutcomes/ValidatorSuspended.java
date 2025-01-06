package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The id of a validator that got suspended due to too many missed rounds.
 */
@ToString
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class ValidatorSuspended extends SpecialOutcome {

    /**
     * The id of the suspended validator.
     */
    private final BakerId bakerId;

    /**
     * The account of the suspended validator.
     */
    private final AccountAddress account;
}
