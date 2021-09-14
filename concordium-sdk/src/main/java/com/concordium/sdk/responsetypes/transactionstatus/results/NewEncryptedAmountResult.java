package com.concordium.sdk.responsetypes.transactionstatus.results;

import com.concordium.sdk.responsetypes.transactionstatus.TransactionResultEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewEncryptedAmountResult  extends TransactionResultEvent {
    private String account;
    private String newIndex;
    private String encryptedAmount;
}
