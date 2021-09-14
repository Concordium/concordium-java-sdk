package com.concordium.sdk.responsetypes.transactionstatus;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionSummary {
    private int index;
    private String hash;
    private String sender;
    private String cost;
    private int energyCost;

    private TransactionResult result;
    private TransactionTypeInfo type;
}
