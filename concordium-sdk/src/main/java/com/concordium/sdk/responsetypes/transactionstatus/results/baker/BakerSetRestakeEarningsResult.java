package com.concordium.sdk.responsetypes.transactionstatus.results.baker;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class BakerSetRestakeEarningsResult  extends AbstractBakerResult {
    private boolean restakeEarnings;
}
