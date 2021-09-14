package com.concordium.sdk.responsetypes.transactionstatus.results.baker;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AbstractBakerChangeResult extends AbstractBakerResult {
    private String electionKey;
    private String aggregationKey;
    private String signKey;
}
