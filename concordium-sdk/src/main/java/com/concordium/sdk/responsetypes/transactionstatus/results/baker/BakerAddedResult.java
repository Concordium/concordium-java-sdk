package com.concordium.sdk.responsetypes.transactionstatus.results.baker;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BakerAddedResult extends AbstractBakerChangeResult {
    private boolean restakeEarnings;
    private String stake;
}
