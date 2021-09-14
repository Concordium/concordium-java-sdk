package com.concordium.sdk.responsetypes.accountinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Baker {
    private int bakerId;
    private String stakedAmount;
    private boolean restakeEarnings;
    private String bakerElectionVerifyKey;
    private String bakerSignatureVerifyKey;
    private String bakerAggregationVerifyKey;
}
