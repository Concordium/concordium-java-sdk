package com.concordium.sdk.responsetypes.transactionstatus.results.baker;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BakerStakeIncreasedResult  extends AbstractBakerResult {
    private String newStake;
}
