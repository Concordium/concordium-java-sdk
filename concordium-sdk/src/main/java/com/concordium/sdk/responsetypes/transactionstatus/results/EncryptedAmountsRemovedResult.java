package com.concordium.sdk.responsetypes.transactionstatus.results;

import com.concordium.sdk.responsetypes.transactionstatus.TransactionResultEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EncryptedAmountsRemovedResult extends TransactionResultEvent {
    private int upToIndex;
    private String account;
    private String inputAmount;
    private String newAmount;
}
