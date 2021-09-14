package com.concordium.sdk.responsetypes.transactionstatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class RejectReason {
    private RejectReasonType tag;
    private Object contents;
}
