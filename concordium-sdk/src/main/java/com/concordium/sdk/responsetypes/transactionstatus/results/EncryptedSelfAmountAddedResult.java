package com.concordium.sdk.responsetypes.transactionstatus.results;

import com.concordium.sdk.responsetypes.transactionstatus.TransactionResultEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EncryptedSelfAmountAddedResult  extends TransactionResultEvent {
    private String amount;
    private String account;
    private String newAmount;
}
