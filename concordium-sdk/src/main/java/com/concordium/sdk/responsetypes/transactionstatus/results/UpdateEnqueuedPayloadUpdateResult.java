package com.concordium.sdk.responsetypes.transactionstatus.results;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateEnqueuedPayloadUpdateResult {
    private String specificationHash;
    private String specificationAuxiliaryData;
    private String message;
    private String specificationURL;
}
